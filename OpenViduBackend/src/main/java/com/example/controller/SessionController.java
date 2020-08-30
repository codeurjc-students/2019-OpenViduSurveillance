package com.example.controller;

import com.example.com.example.error.CameraAlreadyInSessionException;
import com.example.entity.Camera;
import com.example.entity.User;
import com.example.repository.CameraRepository;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class  SessionController {
    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final CameraRepository cameraRepository;

    public SessionController(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

    @Autowired
    UserService userService;

    @PostConstruct
    private void init() {
        //Setting up the HttpClient for correct communication with OpenVidu-Server
        TrustStrategy trustStrategy = (chain, authType) -> true;
        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, trustStrategy).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(30000);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(30000);

        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestBuilder.build())
                .setConnectionTimeToLive(30, TimeUnit.SECONDS).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(sslContext).build();
    }

    @GetMapping("/login")
    public boolean login() {
        return true;
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity start(@PathVariable String sessionId) throws IOException {
        //Validate session name
        try {
            String pattern = "^[a-zA-Z0-9]+$";
            if (sessionId == null || sessionId.isEmpty() || sessionId.equals("undefined")) {
                throw new IllegalArgumentException("Session name can't be empty");
            } else if (!sessionId.matches(pattern)) {
                throw new IllegalArgumentException("Session name can only be alphanumeric without spaces");
            }
//        Request for both token and session at the same time
            HttpPost requestSession = new HttpPost("https://localhost:4443/api/sessions");
            HttpPost requestToken = new HttpPost("https://localhost:4443/api/tokens");

            requestToken.setHeader(HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + "MY_SECRET").getBytes()));
            requestSession.setHeader(HttpHeaders.AUTHORIZATION,
                    "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + "MY_SECRET").getBytes()));

            JSONObject jsonToken = new JSONObject();
            JSONObject jsonSession = new JSONObject();

            jsonToken.put("session", sessionId);
            jsonSession.put("customSessionId", sessionId);

            requestToken.setEntity(new StringEntity(jsonToken.toString()));
            requestSession.setEntity(new StringEntity(jsonSession.toString()));

            requestToken.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            requestSession.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            try (CloseableHttpResponse response = httpClient.execute(requestSession)) {
                HttpEntity entity = response.getEntity();
                Header headers = entity.getContentType();
                System.out.println(headers);
            }
            try (CloseableHttpResponse response = httpClient.execute(requestToken)) {
                System.out.println(response.getStatusLine().toString());
                HttpEntity entity = response.getEntity();
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);

                //Check if this session has cameras in the database
                if (cameraRepository.existsCameraBySession(sessionId)) {
                    String sessionInfo = sessionInfo(sessionId);
                    JsonNode jsonNode = objectMapper.readTree(sessionInfo);
                    int totalConnections = Integer.parseInt(jsonNode.get("connections").get("numberOfElements").toString());
                    if (totalConnections != 0) {
                        JsonNode connections = jsonNode.get("connections").get("content");
                        for (JsonNode j : connections) {
                            JsonNode jsonNode1 = objectMapper.readTree(j.get("publishers").toString());
                            String url = jsonNode1.get(0).get("rtspUri").asText();
                            if (!cameraRepository.existsCameraBySessionAndRtspUri(sessionId, url)) {
                                addAllCamerasBySession(cameraRepository.getCamerasBySession(sessionId), sessionId);
                            }
                        }
                        return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body(result);
                    } else {
                        addAllCamerasBySession(cameraRepository.getCamerasBySession(sessionId), sessionId);
                        return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body(result);
                    }
                }
                return ResponseEntity.status(HttpStatus.SC_ACCEPTED).body(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Session name can only be alphanumeric without spaces");
    }

    @GetMapping("/session/{sessionId}/info")
    public String sessionInfo(@PathVariable String sessionId) throws IOException {

        HttpGet request = new HttpGet("https://localhost:4443/api/sessions/" + sessionId);
        request.setHeader(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + "MY_SECRET").getBytes()));
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        }
    }

    public void addAllCamerasBySession(List<Camera> cameras, String session) {
        for (Camera c : cameras
        ) {
            System.out.println(c.toString());
            newCamera(session, c);
        }
    }

    public String newCamera(@PathVariable String sessionId, @RequestBody Camera camera) {
        HttpPost request = new HttpPost("https://localhost:4443/api/sessions/" + sessionId + "/connection");
        request.setHeader(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + "MY_SECRET").getBytes()));
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        try {
            String json = objectMapper.writeValueAsString(camera);
            System.out.println(json);
            request.setEntity(new StringEntity(json));
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                Header headers = entity.getContentType();
                // return it as a String
                String result = EntityUtils.toString(entity);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/session/{sessionId}/camera")
    public JsonNode addCamera(@RequestBody Camera camera, @PathVariable String sessionId) throws CameraAlreadyInSessionException, JsonProcessingException {
        if (cameraRepository.existsCameraBySessionAndRtspUri(sessionId, camera.getRtspUri())) {
            throw new CameraAlreadyInSessionException("This camera already exist in this session");
        }
        if (camera.getData() == null || camera.getRtspUri() == null) {
            throw new IllegalArgumentException("Camera name and URL can't be empty");
        }
        String pattern = "^[a-zA-Z0-9]+$";
        if (!camera.getData().matches(pattern)) {
            throw new IllegalArgumentException("Camera name can only be alphanumeric");
        }
        if (!camera.getRtspUri().startsWith("rtsp://")) {
            throw new IllegalArgumentException("Camera URL has to start with 'rtsp://'");
        }
        String result = newCamera(sessionId, camera);
        JsonNode jsonNode = objectMapper.readTree(result);
        String connectionId = jsonNode.get("connectionId").toString();
        Camera newCamera = new Camera(camera.getRtspUri(),
                camera.getData(),
                sessionId,
                connectionId);
        cameraRepository.save(newCamera);
        return jsonNode;
    }

    //    Method to delete cameras from the database and the session
    @DeleteMapping("/session/{sessionId}/cameras/{cameraName}")
    public void deleteCamera(@PathVariable String cameraName, @PathVariable String sessionId) throws IOException {
        Camera cam = cameraRepository.getCameraBySessionAndData(sessionId, cameraName);
        String formattedId = cam.getConnectionId().substring(1, cam.getConnectionId().length() - 1);
        HttpDelete request = new HttpDelete("https://localhost:4443/api/sessions/" + sessionId + "/connection/" + formattedId);
        request.setHeader(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + "MY_SECRET").getBytes()));
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        System.out.println("Deleting " + cam.getData() + "-" + cam.getRtspUri() + " from " + cam.getSession());
        cameraRepository.delete(cam);
        httpClient.execute(request);
    }

    @GetMapping("/session/{sessionId}/cameras")
    public List<Camera> getCameras(@PathVariable String sessionId) {
        return cameraRepository.getCamerasBySession(sessionId);
    }

    @PostMapping("/session/{sessionId}/cameras/demo")
    public void addDemoCameras(@PathVariable String sessionId, HttpServletResponse httpServletResponse) throws
            DuplicateKeyException, IOException {
        try {
            if (cameraRepository.existsCameraBySessionAndRtspUri(sessionId, "rtsp://freja.hiof.no:1935/rtplive/_definst_/hessdalen03.stream")) {
                throw new CameraAlreadyInSessionException("This camera already exist in session");
            } else {
                Camera demo1 = new Camera("rtsp://freja.hiof.no:1935/rtplive/_definst_/hessdalen03.stream", "Hessdalen", sessionId);
                Camera demo2 = new Camera("rtsp://170.93.143.139/rtplive/470011e600ef003a004ee33696235daa", "Highway", sessionId);
                addCamera(demo1, sessionId);
                addCamera(demo2, sessionId);
            }
        } catch (CameraAlreadyInSessionException e) {
            System.out.println("ERROR: " + e.getMessage());
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage() + " '" + sessionId + "'");
        }
    }

//    @GetMapping("/discover/cam")
//    public void discoverCam() throws SOAPException, ConnectException {
//        OnvifDevice nvt = new OnvifDevice("192.168.1.137:8081", "admin", "password");
//
//        List<Profile> profiles = nvt.getDevices().getProfiles();
//        String profileToken = profiles.get(0).getToken();
//
//        PtzDevices ptzDevices = nvt.getPtz(); // get PTZ Devices
//        System.out.println(ptzDevices.getStatus(profileToken));
//        FloatRange panRange = ptzDevices.getPanSpaces(profileToken);
//        FloatRange tiltRange = ptzDevices.getTiltSpaces(profileToken);
//        System.out.println("Max panrange : " + panRange.getMax() + " Max tiltrange: " + tiltRange.getMax());
//        System.out.println(ptzDevices.isContinuosMoveSupported(profileToken));
//        System.out.println(ptzDevices.isAbsoluteMoveSupported(profileToken));
//        System.out.println(ptzDevices.isPtzOperationsSupported(profileToken));
//        for (int i = 0; i <= 5; i++)
//            ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
//    }
//
//    @PostMapping("/ptz/{hostIp}")
//    public boolean ptz(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp) throws SOAPException, ConnectException {
//        String ipUrl = hostIp + ":8081";
//        OnvifDevice onvifDevice = new OnvifDevice(ipUrl, user, password);
//        List<Profile> profiles = onvifDevice.getDevices().getProfiles();
//        String profileToken = profiles.get(0).getToken();
//        return onvifDevice.getPtz().isPtzOperationsSupported(profileToken);
//    }
//
//    @PostMapping("/ptz/{hostIp}/{direction}")
//    public void ptzMovement(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp, @PathVariable String direction) throws SOAPException, ConnectException {
//        String ipUrl = hostIp + ":8081";
//        OnvifDevice onvifDevice = new OnvifDevice(ipUrl, user, password);
//        List<Profile> profiles = onvifDevice.getDevices().getProfiles();
//        String profileToken = profiles.get(0).getToken();
//        PtzDevices ptzDevices = onvifDevice.getPtz(); // get PTZ Devices
//        switch (direction) {
//            case "up":
//                for (int i = 0; i <= 5; i++)
//                    ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
//                break;
//            case "down":
//                for (int i = 0; i <= 5; i++)
//                    ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
//                break;
//            case "left":
//                for (int i = 0; i <= 5; i++)
//                    ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
//                break;
//            case "right":
//                for (int i = 0; i <= 5; i++)
//                    ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
//                break;
//
//        }
//    }
}

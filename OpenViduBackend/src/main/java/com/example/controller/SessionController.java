package com.example.controller;

import com.example.com.example.error.CameraAlreadyInSessionException;
import com.example.entity.Camera;
import com.example.entity.IpCamera;
import com.example.repository.CameraRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import org.springframework.dao.DuplicateKeyException;
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
public class SessionController {
    private CloseableHttpClient httpClient;

    private final CameraRepository cameraRepository;

    public SessionController(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

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


    @PostMapping("/session/{sessionId}")
    public String start(@PathVariable String sessionId) throws IOException {
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
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);
            // return it as a String
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
        try (CloseableHttpResponse response = httpClient.execute(requestToken)) {
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);
            // return it as a String
            String result = EntityUtils.toString(entity);
            System.out.println(result);
            if (cameraRepository.existsCameraBySession(sessionId)) {
                String sessionInfo = sessionInfo(sessionId);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(sessionInfo);
                int totalConnections = Integer.parseInt(jsonNode.get("connections").get("numberOfElements").toString());
                if (totalConnections != 0) {
                    System.out.println("Intentando ");
                    JsonNode connections = jsonNode.get("connections").get("content");
                    for (JsonNode j : connections) {
                        JsonNode jsonNode1 = objectMapper.readTree(j.get("publishers").toString());
                        String url = jsonNode1.get(0).get("rtspUri").asText();
                        System.out.println(url);
                        System.out.println(j.get("publishers").asText());
                        if (!cameraRepository.existsCameraBySessionAndUrl(sessionId, url)) {
                            System.out.println("Añadiendo camaras desde el if");
                            addAllCamerasBySession(cameraRepository.getCamerasBySession(sessionId), sessionId);
                        }
                    }
                    return result;
                } else {
                    System.out.println("Añadiendo camaras");
                    addAllCamerasBySession(cameraRepository.getCamerasBySession(sessionId), sessionId);
                    return result;
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            Header headers = entity.getContentType();
            System.out.println(headers);

            // return it as a String
            String result = EntityUtils.toString(entity);
            System.out.println(result);
            return result;
        }
    }

    public void addAllCamerasBySession(List<Camera> cameras, String session) {
        for (Camera c : cameras
        ) {
            System.out.println(c.toString());
            IpCamera ipCamera = new IpCamera(c.getUrl(), c.getCamera());
            newCamera(session, ipCamera);
        }
    }

    public String newCamera(@PathVariable String sessionId, @RequestBody IpCamera ipCamera) {
                ObjectMapper objectMapper = new ObjectMapper();
                HttpPost request = new HttpPost("https://localhost:4443/api/sessions/" + sessionId + "/connection");
                request.setHeader(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + "MY_SECRET").getBytes()));
                request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

                try {
                    String json = objectMapper.writeValueAsString(ipCamera);
                    System.out.println(json);
                    request.setEntity(new StringEntity(json));
                    try (CloseableHttpResponse response = httpClient.execute(request)) {
                        System.out.println(response.getStatusLine().toString());

                        HttpEntity entity = response.getEntity();
                        Header headers = entity.getContentType();
                        System.out.println(headers);
                        // return it as a String
                        String result = EntityUtils.toString(entity);
                        System.out.println(result);
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
    public String addCamera(@RequestBody IpCamera ipCamera, @PathVariable String sessionId ) throws CameraAlreadyInSessionException {
        if (cameraRepository.existsCameraBySessionAndUrl(sessionId, ipCamera.rtspUri)) {
            throw new CameraAlreadyInSessionException("This camera already exist in this session");
        }
        Camera camera = new Camera(ipCamera.rtspUri, ipCamera.data, sessionId);
        newCamera(sessionId, ipCamera);
        cameraRepository.save(camera);
        return camera.toString();
    }

    @GetMapping("/session/{sessionId}/cameras")
    public List<Camera> getCameras(@PathVariable String sessionId) {
        return cameraRepository.getCamerasBySession(sessionId);
    }

    @PostMapping("/session/{sessionId}/cameras/demo")
    public void addDemoCameras(@PathVariable String sessionId, HttpServletResponse httpServletResponse) throws DuplicateKeyException, IOException {
        try {
            if (cameraRepository.existsCameraBySessionAndUrl(sessionId, "rtsp://freja.hiof.no:1935/rtplive/_definst_/hessdalen03.stream")) {
                throw new CameraAlreadyInSessionException("This camera already exist in this session");
            } else {
                IpCamera demo1= new IpCamera("rtsp://freja.hiof.no:1935/rtplive/_definst_/hessdalen03.stream", "Hessdalen",true,true);
                IpCamera demo2= new IpCamera("rtsp://170.93.143.139/rtplive/470011e600ef003a004ee33696235daa", "Hessdalen",true,true);
                newCamera(sessionId,demo1);
                newCamera(sessionId,demo2);
            }
        } catch (CameraAlreadyInSessionException e) {
            System.out.println("ERROR: " + e.getMessage());
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "This cameras already exist in the session: " + sessionId);
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

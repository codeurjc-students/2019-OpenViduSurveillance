package com.example.controller;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.*;

import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.listeners.DiscoveryListener;
import be.teletask.onvif.models.Device;

import com.example.entity.IpCamera;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.onvif.soap.OnvifDevice;
import de.onvif.soap.devices.PtzDevices;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
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
import org.onvif.ver10.schema.FloatRange;
import org.onvif.ver10.schema.Profile;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.*;

@CrossOrigin
@RestController
public class SessionController {
    private CloseableHttpClient httpClient;

    @PostConstruct
    private void init() {
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };

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

    @GetMapping("/saludo")
    public String index() {
        return "Funcionando";
    }

    @GetMapping("/session/{sessionId}")
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

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                return result;
            }
        }
        return "nulo";
    }

    @GetMapping("/newSession/{sessionId}")
    public String start(@PathVariable String sessionId) throws IOException {
//        "Duplicate all methods to make both request at the same time"
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
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        }
        try (CloseableHttpResponse response = httpClient.execute(requestToken)) {
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/session/{sessionId}/addIpCamera")
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
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                    return result;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/discover/cam")
    public void discoverCam() throws SOAPException, ConnectException {
        OnvifDevice nvt = new OnvifDevice("192.168.1.137:8081", "admin", "password");

        List<Profile> profiles = nvt.getDevices().getProfiles();
        String profileToken = profiles.get(0).getToken();

        PtzDevices ptzDevices = nvt.getPtz(); // get PTZ Devices
        System.out.println(ptzDevices.getStatus(profileToken));
        FloatRange panRange = ptzDevices.getPanSpaces(profileToken);
        FloatRange tiltRange = ptzDevices.getTiltSpaces(profileToken);
        System.out.println("Max panrange : " + panRange.getMax() + " Max tiltrange: " + tiltRange.getMax());
        System.out.println(ptzDevices.isContinuosMoveSupported(profileToken));
        System.out.println(ptzDevices.isAbsoluteMoveSupported(profileToken));
        System.out.println(ptzDevices.isPtzOperationsSupported(profileToken));
        for (int i = 0; i <= 5; i++)
            ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
    }

    @PostMapping("/ptz/{hostIp}")
    public boolean ptz(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp) throws SOAPException, ConnectException {
        String ipUrl = hostIp + ":8081";
        OnvifDevice onvifDevice = new OnvifDevice(ipUrl, user, password);
        List<Profile> profiles = onvifDevice.getDevices().getProfiles();
        String profileToken = profiles.get(0).getToken();
        return onvifDevice.getPtz().isPtzOperationsSupported(profileToken);
    }

    @PostMapping("/ptz/{hostIp}/{direction}")
    public void ptzMovement(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp, @PathVariable String direction) throws SOAPException, ConnectException {
        String ipUrl = hostIp + ":8081";
        OnvifDevice onvifDevice = new OnvifDevice(ipUrl, user, password);
        List<Profile> profiles = onvifDevice.getDevices().getProfiles();
        String profileToken = profiles.get(0).getToken();
        PtzDevices ptzDevices = onvifDevice.getPtz(); // get PTZ Devices
        if (direction == "up") {
            for (int i = 0; i <= 5; i++)
                ptzDevices.absoluteMove(profileToken, (float) 1, (float) 1, 1);
        }
    }
}

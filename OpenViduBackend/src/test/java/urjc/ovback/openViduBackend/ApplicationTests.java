package urjc.ovback.openViduBackend;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.junit.Test;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class ApplicationTests {
    //Put here your OpenVidu Server URL
    public String OPENVIDU_URL = "https://localhost:4443";

    @Test()
    public void openViduServerConnection() {
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
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestBuilder.build())
                .setConnectionTimeToLive(30, TimeUnit.SECONDS).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLContext(sslContext).build();
        HttpGet httpGet = new HttpGet(OPENVIDU_URL);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            assert (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
        } catch (IOException e) {
            assert e == null;
        }
    }

}

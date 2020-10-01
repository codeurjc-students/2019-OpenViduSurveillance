package urjc.ovback.entity;

import javax.persistence.*;

@Entity
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String session;
    private String rtspUri;

    @Column(unique = true, nullable = false)
    private String data;
    private String connectionId;
    private String cameraIp;
    private int port;
    private Boolean ptzAvailable;
    private String cameraUser;
    private String cameraPassword;

    protected Camera() {

    }

    public Camera(String rtspUri, String cameraName, String sessionName) {
        this.rtspUri = rtspUri;
        this.session = sessionName;
        this.data = cameraName;
    }

    public Camera(String rtspUri, String cameraName, String sessionName, String connectionId) {
        this.rtspUri = rtspUri;
        this.session = sessionName;
        this.data = cameraName;
        this.connectionId = connectionId;
    }

    public Camera(String session, String rtspUri, String data, String connectionId, String cameraIp, int port, boolean ptzAvailable) {
        this.session = session;
        this.rtspUri = rtspUri;
        this.data = data;
        this.connectionId = connectionId;
        this.cameraIp = cameraIp;
        this.port = port;
        this.ptzAvailable = ptzAvailable;
    }

    public Camera(String session, String rtspUri, String data, String connectionId, String cameraIp, int port, String user, String password) {
        this.session = session;
        this.rtspUri = rtspUri;
        this.data = data;
        this.connectionId = connectionId;
        this.cameraIp = cameraIp;
        this.port = port;
        this.cameraUser = user;
        this.cameraPassword = password;
        this.ptzAvailable = true;
    }

    public Camera(String rtspUri) {
        this.rtspUri = rtspUri;
    }

    public Camera(String rtspUri, String cameraName) {
        this.rtspUri = rtspUri;
        this.data = cameraName;
    }

    public String getSession() {
        return session;
    }

    public String getRtspUri() {
        return rtspUri;
    }

    public String getData() {
        return data;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setRtspUri(String rtspUri) {
        this.rtspUri = rtspUri;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public void setCameraIp(String cameraIp) {
        this.cameraIp = cameraIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Boolean getPtzAvailable() {
        return ptzAvailable;
    }

    public void setPtzAvailable(Boolean ptzAvailable) {
        this.ptzAvailable = ptzAvailable;
    }

    public String getCameraUser() {
        return cameraUser;
    }

    public void setCameraUser(String cameraUser) {
        this.cameraUser = cameraUser;
    }

    public String getCameraPassword() {
        return cameraPassword;
    }

    public void setCameraPassword(String cameraPassword) {
        this.cameraPassword = cameraPassword;
    }
}

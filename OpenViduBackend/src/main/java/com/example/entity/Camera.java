package com.example.entity;

import javax.persistence.*;

@Entity
public class Camera {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String session;
    private String rtspUri;
    private String data;
    private String connectionId;

    protected Camera(){

    }

    public Camera(String rtspUri, String cameraName, String sessionName){
        this.rtspUri = rtspUri;
        this.session = sessionName;
        this.data = cameraName;
    }
    public Camera(String rtspUri, String cameraName, String sessionName, String connectionId){
        this.rtspUri = rtspUri;
        this.session = sessionName;
        this.data = cameraName;
        this.connectionId = connectionId;
    }

    public Camera(String rtspUri){
        this.rtspUri = rtspUri;
    }

    public Camera(String rtspUri, String cameraName){
        this.rtspUri = rtspUri;
        this.data = cameraName;
    }

    public Long getId() {
        return id;
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
                //.substring(1,connectionId.length()-1);

    }
    //    public String toString(){
//        return "Camera :" + camera + " - URL :" + url + " - Session :" + session;
//    }
}

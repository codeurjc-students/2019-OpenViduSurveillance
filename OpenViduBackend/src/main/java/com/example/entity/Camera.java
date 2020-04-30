package com.example.entity;

import javax.persistence.*;

@Entity
public class Camera {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String session;
    private String url;
    private String camera;

    protected Camera(){

    }

    public Camera(String url, String cameraName, String sessionName){
        this.url = url;
        this.session = sessionName;
        this.camera = cameraName;
    }

    public Camera(String url){
        this.url = url;
    }

    public Camera(String url, String cameraName){
        this.url = url;
        this.camera = cameraName;
    }

    public Long getId() {
        return id;
    }

    public String getSession() {
        return session;
    }

    public String getUrl() {
        return url;
    }

    public String getCamera() {
        return camera;
    }
}

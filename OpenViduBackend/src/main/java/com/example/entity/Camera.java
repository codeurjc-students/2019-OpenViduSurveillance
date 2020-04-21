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

}

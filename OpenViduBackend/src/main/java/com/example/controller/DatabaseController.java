package com.example.controller;

import com.example.entity.Camera;
import com.example.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DatabaseController {

    @Autowired
    private CameraRepository cameraRepository;

    @PostMapping("/add")
    public String addNewCamera(@RequestBody String url){
        Camera camera = new Camera(url);
        cameraRepository.save(camera);
        return camera.toString();
    }

    public String addCamera(String url, String sessionName,String cameraName){
        Camera camera = new Camera(url,cameraName,sessionName);
        cameraRepository.save(camera);
        return camera.toString();
    }

    @PostMapping("/addDemoCameras")
    public void addDemoCameras(@RequestBody String sessionName){
        addCamera("rtsp://freja.hiof.no:1935/rtplive/_definst_/hessdalen03.stream","Hessdalen",sessionName);
        addCamera("rtsp://170.93.143.139/rtplive/470011e600ef003a004ee33696235daa","Highway",sessionName);

    }
}


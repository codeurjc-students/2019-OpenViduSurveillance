package com.example.controller;

import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.listeners.DiscoveryListener;
import be.teletask.onvif.models.Device;
import com.example.entity.Camera;
import com.example.entity.IpCamera;
import com.example.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.*;

import javax.xml.soap.SOAPException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@CrossOrigin
@RestController
public class DatabaseController {

    @Autowired
    private CameraRepository cameraRepository;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

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
        addCamera("rtsp://freja.hiof.no:1935/rtplive/_definst_/hessdalen03.stream",sessionName,"Hessdalen");
        addCamera("rtsp://170.93.143.139/rtplive/470011e600ef003a004ee33696235daa",sessionName,"Highway");
    }

    @PostMapping("/discover")
    public String  discoverCameras(@RequestBody String sessionName) {
        Future future = discover(sessionName);
        while(!future.isDone()){
        }
        return "Local cameras added to the database with the name 'localNetworkCamera'";
    }
    public Future discover(String sessionName) {
        return executor.submit(() -> {
            DiscoveryManager manager = new DiscoveryManager();

            manager.setDiscoveryTimeout(5000);
            manager.discover(new DiscoveryListener() {
                @Override
                public void onDiscoveryStarted() {
                    System.out.println("Discovery started");
                }

                @Override
                public void onDevicesFound(List<Device> devices) {
                    for (Device device : devices) {
                        System.out.println("Devices found: " + device.getHostName() + " " + device.getUsername());
                        Camera camera = new Camera(device.getHostName().substring(7),"localNetworkCamera",sessionName);
                        cameraRepository.save(camera);
                    }
                }
            });
            try {
                //Executor service sends the "done signal" before the discovering really finishes, so
                // we wait for the discovery to really finish before sending the list of devices
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @GetMapping("/localCameras")
    public List<Camera> localCameras(){
        List<Camera> cameras = cameraRepository.getCamerasByCamera("localNetworkCamera");
//        List<String> list = new ArrayList<>();
//        for (Camera c: cameras) {
//            list.add(c.getUrl());
//        }
        return cameras;
    }

}


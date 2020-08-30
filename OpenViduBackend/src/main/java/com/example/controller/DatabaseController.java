package com.example.controller;

import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.listeners.DiscoveryListener;
import be.teletask.onvif.models.Device;
import com.example.entity.Camera;
import com.example.repository.CameraRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@CrossOrigin
@RestController
public class DatabaseController {

    private final CameraRepository cameraRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DatabaseController(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

    @PostMapping("/localCameras")
    public String discoverCameras(@RequestBody String sessionName) {
        Future future = discover(sessionName);
        while (!future.isDone()) {
        }
        return "Local cameras added to the database with the name 'localNetworkCamera'";
    }

    public Future discover(String sessionName) {
        return executor.submit(() -> {
            DiscoveryManager manager = new DiscoveryManager();

            manager.setDiscoveryTimeout(10000);
            manager.discover(new DiscoveryListener() {
                @Override
                public void onDiscoveryStarted() {
                    System.out.println("Discovery started");
                }

                @Override
                public void onDevicesFound(List<Device> devices) {
                    for (Device device : devices) {
                        System.out.println("Devices found: " + device.getHostName() + " " + device.getUsername());
                        Camera camera = new Camera(device.getHostName().substring(7), "localNetworkCamera", sessionName);
                        if (!cameraRepository.existsCameraBySessionAndRtspUri(sessionName, device.getHostName().substring(7)))
                            cameraRepository.save(camera);
                    }
                }
            });
            try {
                //Executor service sends the "done signal" before the discovering really finishes, so
                // we wait for the discovery to really finish before sending the list of devices
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @GetMapping("/cameras/local")
    public List<Camera> localCameras() {
        return cameraRepository.getCamerasByData("localNetworkCamera");
    }

}


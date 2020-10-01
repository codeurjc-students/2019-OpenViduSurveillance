package urjc.ovback.controller;

import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.listeners.DiscoveryListener;
import be.teletask.onvif.models.Device;
import urjc.ovback.entity.Camera;
import urjc.ovback.repository.CameraRepository;
import de.onvif.soap.OnvifDevice;
import de.onvif.soap.devices.PtzDevices;
import org.onvif.ver10.schema.Profile;
import org.springframework.web.bind.annotation.*;

import javax.xml.soap.SOAPException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@CrossOrigin
@RestController
public class CamerasPTZController {

    private final CameraRepository cameraRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CamerasPTZController(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }
    public final ArrayList<Camera> localCameras = new ArrayList<>();


    @GetMapping("/{sessionName}/localCameras")
    public List<Camera> discoverCameras(@PathVariable String sessionName) {
        Future future = discover(sessionName);
        while (!future.isDone()) {
        }
        System.out.println(cameraRepository.getCamerasBySessionAndData(sessionName, "localNetworkCamera"));
        return localCameras;

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
                        if (!cameraRepository.existsCameraBySessionAndRtspUri(sessionName, device.getHostName().substring(7))) {
                            System.out.println(camera);
                            localCameras.add(camera);
                        }
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
    @GetMapping("{sessionName}/cameras/{cameraName}/ptz")
    public boolean ptzAvailable(@PathVariable String sessionName, @PathVariable String cameraName) throws SOAPException, ConnectException {
        Camera cam = cameraRepository.getCameraBySessionAndData(sessionName, cameraName);
        if (cam.getPtzAvailable() != null && cam.getPtzAvailable()) {
            String ipUrl = cam.getCameraIp() + ":" + cam.getPort();
            OnvifDevice onvifDevice = new OnvifDevice(ipUrl, cam.getCameraUser(), cam.getCameraPassword());
            List<Profile> profiles = onvifDevice.getDevices().getProfiles();
            String profileToken = profiles.get(0).getToken();
            return onvifDevice.getPtz().isPtzOperationsSupported(profileToken);
        } else
            return false;
    }

    @GetMapping("{sessionName}/cameras/{cameraName}/{direction}")
    public void ptzMovement(@PathVariable String sessionName, @PathVariable String cameraName, @PathVariable String direction) throws SOAPException, ConnectException {
        Camera cam = cameraRepository.getCameraBySessionAndData(sessionName, cameraName);
        String ipUrl = cam.getCameraIp() + ":" + cam.getPort();
        OnvifDevice onvifDevice = new OnvifDevice(ipUrl, cam.getCameraUser(), cam.getCameraPassword());
        List<Profile> profiles = onvifDevice.getDevices().getProfiles();
        String profileToken = profiles.get(0).getToken();
        PtzDevices ptzDevices = onvifDevice.getPtz(); // get PTZ Devices
        switch (direction) {
            case "up":
                for (int i = 0; i <= 2; i++) {
                    ptzDevices.absoluteMove(profileToken, 0, 1, 1);
                }
                break;
            case "down":
                for (int i = 0; i <= 2; i++)
                    ptzDevices.absoluteMove(profileToken, 0, -1, 1);
                break;
            case "left":
                for (int i = 0; i <= 2; i++)
                    ptzDevices.absoluteMove(profileToken, -1, 0, 1);
                break;
            case "right":
                for (int i = 0; i <= 2; i++)
                    ptzDevices.absoluteMove(profileToken, 1, 0, 1);
                break;
        }
    }
}


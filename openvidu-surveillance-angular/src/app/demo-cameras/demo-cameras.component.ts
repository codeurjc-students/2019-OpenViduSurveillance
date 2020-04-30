import {Component, Input, OnInit} from '@angular/core';
import {CameraService} from '../camera.service';
import {FormControl, FormGroup} from '@angular/forms';
import {CameraDevice} from "../camera-device";

@Component({
    selector: 'app-demo-cameras',
    templateUrl: './demo-cameras.component.html',
    styleUrls: ['./demo-cameras.component.css']
})
export class DemoCamerasComponent implements OnInit {
    devices: CameraDevice[];
    @Input() sessionId: string;
    networkDevicesIp: string[];
    form = new FormGroup({
        devices: new FormControl(this.cameraService.availableIpCameras)
    })
    selectedDevice: CameraDevice;

    constructor(private cameraService: CameraService) {
        this.devices = cameraService.availableIpCameras;
    }

    ngOnInit() {

    }

    addDemoCameras() {
        this.cameraService.publishDemoCameras(this.sessionId);
    }

    setDevice(device: CameraDevice) {
        this.selectedDevice = device;
        console.log(device);
    }

}

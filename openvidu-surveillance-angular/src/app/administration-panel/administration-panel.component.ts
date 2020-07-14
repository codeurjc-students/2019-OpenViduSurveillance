import {Component, Input} from '@angular/core';
import {CameraService} from '../camera.service';
import {FormControl, FormGroup} from '@angular/forms';
import {CameraDevice} from '../camera-device';

@Component({
    selector: 'app-demo-cameras',
    templateUrl: './administration-panel.component.html',
    styleUrls: ['./administration-panel.component.css']
})
export class AdministrationPanelComponent {
    devices: CameraDevice[];
    @Input() sessionId: string;

    form = new FormGroup({
        devices: new FormControl(this.cameraService.availableIpCameras)
    })
    selectedDevice: CameraDevice;
    hide = true;
    cameraName: String;
    cameraUrl: String

    constructor(public cameraService: CameraService) {
        this.devices = cameraService.availableIpCameras;
    }

    addDemoCameras() {
        this.cameraService.publishDemoCameras(this.sessionId);
    }

}

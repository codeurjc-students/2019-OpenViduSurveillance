import {Component, Input, OnInit} from '@angular/core';
import {StreamManager} from 'openvidu-browser';
import {CameraService} from '../services/camera.service';


@Component({
    selector: 'app-session-cameras',
    templateUrl: './session-cameras.component.html',
    styleUrls: ['./session-cameras.component.css']
})
export class SessionCamerasComponent {
    @Input() sessionId: string;
    @Input() subscribers: StreamManager[] = [];

    constructor(public cameraService: CameraService) {
    }

    updateMainStreamManager(streamManager: StreamManager) {
        this.cameraService.mainStreamManager = streamManager;
        console.log('Updating: ' + streamManager.id);
    }
}

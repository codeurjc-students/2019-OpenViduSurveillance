import {Component, Input, OnInit} from '@angular/core';
import {Session} from '../session';
import {SessionInfoService} from '../session-info.service';
import {StreamManager} from 'openvidu-browser';
import {CameraService} from "../camera.service";


@Component({
    selector: 'app-session-info',
    templateUrl: './session-info.component.html',
    styleUrls: ['./session-info.component.css']
})
export class SessionInfoComponent implements OnInit {

    @Input() sessionId: string;
    session: Session;
    @Input() subscribers: StreamManager[] = [];

    constructor(public sessionInfoService: SessionInfoService,
                private cameraService: CameraService) {
    }

    ngOnInit() {
       this.sessionInfoService.getSessionInfo(this.sessionId).subscribe(response => {
            this.session = JSON.parse(response)
       }, error => console.log(error));
    }

    updateMainStreamManager(streamManager: StreamManager) {
        this.cameraService.mainStreamManager = streamManager;
        console.log('Updating: ' + streamManager.id);
    }

}

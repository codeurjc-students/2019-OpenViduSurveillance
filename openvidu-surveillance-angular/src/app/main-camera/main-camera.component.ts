import {Component, DoCheck, Input, KeyValueDiffer, KeyValueDiffers, OnInit} from '@angular/core';
import {StreamManager} from 'openvidu-browser';
import {CameraService} from '../services/camera.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Component({
    selector: 'app-main-camera',
    templateUrl: './main-camera.component.html',
    styleUrls: ['./main-camera.component.css']
})
export class MainCameraComponent implements DoCheck {
    @Input() mainStreamManager: StreamManager;
    ptz: Boolean = false;
    @Input() sessionName: String;
    headers = new HttpHeaders({
        'Authorization': 'Basic ' + sessionStorage.getItem('token'),
        'Content-type': 'application/x-www-form-urlencoded',
    });

    differ: KeyValueDiffer<string, any>;
    private backendUrl = environment.OPENVIDUSURVEILLANCE_BACKEND_URL;

    constructor(public cameraService: CameraService,
                private httpClient: HttpClient,
                private differs: KeyValueDiffers) {
        this.differ = this.differs.find({}).create();

    }

    move(direction: string) {
        const options = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + sessionStorage.getItem('token'),
                'Content-Type': 'application/x-www-form-urlencoded',
                'Response-Type': 'text'
            })
        }
        this.httpClient.get(this.backendUrl + this.sessionName + '/cameras/' +
            this.mainStreamManager.stream.connection.data + '/' + direction,
            options).subscribe(res => {
            console.log(JSON.parse(<string>res));
        })
    }

    ngDoCheck(): void {
        const changed = this.differ.diff(this.mainStreamManager);
        const options = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + sessionStorage.getItem('token'),
                'Content-Type': 'application/x-www-form-urlencoded',
                'Response-Type': 'text'
            })
        }
        if (changed) {
            this.ptz = false;
            this.httpClient.get(this.backendUrl + this.sessionName
                + '/cameras/' + this.mainStreamManager.stream.connection.data + '/ptz',
                options).subscribe(res => {
                this.ptz = JSON.parse(<string>res);
            })
        }
    }
}

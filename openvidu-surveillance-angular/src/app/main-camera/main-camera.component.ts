import {Component, Input, OnInit} from '@angular/core';
import {StreamManager} from 'openvidu-browser';
import {CameraService} from '../camera.service';
import {HttpClient} from '@angular/common/http';

@Component({
    selector: 'app-main-camera',
    templateUrl: './main-camera.component.html',
    styleUrls: ['./main-camera.component.css']
})
export class MainCameraComponent implements OnInit {
    @Input() mainStreamManager: StreamManager;
    ptz: Boolean = false;
    ipUrl: String;

    constructor(public cameraService: CameraService,
                private httpClient: HttpClient) {
    }

    ngOnInit() {
    }

    move(direction: string) {
        this.ipUrl = this.mainStreamManager.stream.connection.data;
        let formData = new FormData();
        formData.append('user', 'admin');
        formData.append('password', 'admin');
        this.httpClient.post('https://localhost:8080/ptz/' + this.ipUrl + '/' + direction, formData, {responseType: 'text'}).subscribe(res => {
            console.log(JSON.parse(res));
        })
    }

    ptzAvailable() {
        this.ipUrl = this.mainStreamManager.stream.connection.data;
        let formData = new FormData();
        formData.append('user', 'admin');
        formData.append('password', 'admin');
        console.log(this.ipUrl);
        this.httpClient.post('https://localhost:8080/ptz/' + this.ipUrl, formData, {responseType: 'text'}).subscribe(res => {
            this.ptz = JSON.parse(res);
        })
    }

}

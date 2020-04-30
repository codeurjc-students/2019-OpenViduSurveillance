import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {throwError as observableThrowError} from 'rxjs/internal/observable/throwError';
import {StreamManager} from 'openvidu-browser';
import {CameraDevice} from './camera-device';
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CameraService {
    private OPENVIDU_SERVER_SECRET = 'MY_SECRET';
    private OPENVIDU_SERVER_URL = 'https://localhost:8080/';
    public mainStreamManager: StreamManager;
    public availableIpCameras: CameraDevice[];

    constructor(public httpClient: HttpClient) {
        this.addLocalCameras();
    }


    publishDemoCameras(mySessionId: string) {
        const options = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa('OPENVIDUAPP:' + this.OPENVIDU_SERVER_SECRET),
                'Content-Type': 'text/plain',
                'Access-Control-Allow-Origin': '*'
            })
        };
        this.httpClient.post('https://localhost:8080/addDemoCameras', mySessionId, options).subscribe(result => {
            console.log(result);
        });
    }

    publishIpCamera(sessionId, rtspUri, cameraName, adaptativeBitrate, onlyPlayWhenSubscribers) {
        return new Promise((resolve, reject) => {
            console.log('Camera ' + rtspUri);
            const body = JSON.stringify({
                rtspUri: rtspUri, data: cameraName, adaptativeBitrate: adaptativeBitrate,
                onlyPlayWithSubscribers: onlyPlayWhenSubscribers
            });
            const options = {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + btoa('OPENVIDUAPP:' + this.OPENVIDU_SERVER_SECRET),
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                })
            };
            // return this.httpClient.post(this.OPENVIDU_SERVER_URL + '/api/sessions/' + sessionId + '/connection', body, options)
            return this.httpClient.post('https://localhost:8080/session/' + sessionId + '/addIpCamera', body, options)
                .pipe(
                    catchError(error => {
                        reject(error)
                        if (error.status === 409) {
                            resolve(sessionId);
                        } else {
                            console.warn('No connection to OpenVidu Server. This may be a certificate error at ' + this.OPENVIDU_SERVER_URL);
                            if (window.confirm('No connection to OpenVidu Server. This may be a certificate error at \"' + this.OPENVIDU_SERVER_URL +
                                '\"\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server' +
                                'is up and running at "' + this.OPENVIDU_SERVER_URL + '"')) {
                                location.assign(this.OPENVIDU_SERVER_URL + '/accept-certificate');
                            }
                        }
                        return observableThrowError(error);
                    })
                )
                .subscribe(response => {
                    console.log(response);
                    resolve(response['id']);
                });
        });
    }

    discoverCameras(sessionID) {
        const options = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa('OPENVIDUAPP:' + this.OPENVIDU_SERVER_SECRET),
                'Content-Type': 'text/plain'
            })
        };
        this.httpClient.post('https://localhost:8080/discover', sessionID, options).subscribe(response => {
            console.log(response);
        });
    }
    addLocalCameras() {
        this.httpClient.get('https://localhost:8080/localCameras', {responseType: 'text'}).subscribe(response => {
            console.log(response);
            console.log(JSON.parse(response));
            this.availableIpCameras = JSON.parse(response);
        })
    }
}

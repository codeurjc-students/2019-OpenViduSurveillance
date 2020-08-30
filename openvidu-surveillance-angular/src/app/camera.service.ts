import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {throwError as observableThrowError} from 'rxjs/internal/observable/throwError';
import {StreamManager} from 'openvidu-browser';
import {CameraDevice} from './camera-device';
import {MatDialog} from '@angular/material/dialog';
import {AlertService} from './_alert';

@Injectable({
    providedIn: 'root'
})
export class CameraService {
    private OPENVIDU_SERVER_SECRET = 'MY_SECRET';
    private OPENVIDU_SERVER_URL = 'https://localhost:8080/';
    public mainStreamManager: StreamManager;
    public availableIpCameras: CameraDevice[];
    public autoCloseAlert = {
        autoClose: true
    }

    constructor(public httpClient: HttpClient, protected alertService: AlertService) {
        // Method not used right now
        // this.addLocalCameras();
    }


    publishDemoCameras(sessionId: string) {
        return new Promise((resolve, reject) => {
            JSON.stringify({
                sessionName: sessionId
            });
            const body = JSON.stringify({
                rtspUri: 'rtspUri', data: 'cameraName', adaptativeBitrate: 'adaptativeBitrate',
                onlyPlayWithSubscribers: 'onlyPlayWhenSubscribers'
            });
            const options = {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + btoa('USER:' + 'Surveillance'),
                    'Content-Type': 'application/json'
                })
            };
            // return this.httpClient.post(this.OPENVIDU_SERVER_URL + '/api/sessions/' + sessionId + '/connection', body, options)

            return this.httpClient.post('https://localhost:8080/session/' + sessionId + '/cameras/demo', body, options)
                // .pipe(
                //     catchError(error => {
                //         if (error.status === 409) {
                //             this.alertService.clear();
                //             this.alertService.success('Demo cameras added correctly', this.autoCloseAlert);
                //             resolve(sessionId);
                //         } else if (error.status === 500) {
                //             // alert('Error adding cameras: ' + JSON.parse(msg.error).message);
                //             this.alertService.clear();
                //             this.alertService.error('Error adding demo cameras, already exist in session', this.autoCloseAlert);
                //             reject(error);
                //         } else {
                //             console.warn('No connection to OpenVidu Server. ' +
                //                 'This may be a certificate error at ' + this.OPENVIDU_SERVER_URL);
                //             if (window.confirm('No connection to OpenVidu Server. ' +
                //                 'This may be a certificate error at \"' + this.OPENVIDU_SERVER_URL +
                //                 '\"\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server' +
                //                 'is up and running at "' + this.OPENVIDU_SERVER_URL + '"')) {
                //                 location.assign(this.OPENVIDU_SERVER_URL + '/accept-certificate');
                //             }
                //         }
                //         return observableThrowError(error.message);
                //     })
                // )
                .subscribe(() => {
                    this.alertService.clear();
                    this.alertService.success('Demo cameras added correctly', this.autoCloseAlert);
                });
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
                    'Authorization': 'Basic ' + btoa('USER:' + 'Surveillance'),
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                })
            };
            return this.httpClient.post('https://localhost:8080/session/' + sessionId + '/camera', body, options)
                .pipe(
                    catchError(error => {
                        reject(error)
                        if (error.status === 409) {
                            resolve(sessionId);
                        } else if (error.status === 500) {
                            console.log(error);
                            this.alertService.clear();
                            this.alertService.error(error.error.message, this.autoCloseAlert);
                        } else {
                            console.warn('No connection to OpenVidu Server. ' +
                                'This may be a certificate error at ' + this.OPENVIDU_SERVER_URL);
                            if (window.confirm('No connection to OpenVidu Server. ' +
                                'This may be a certificate error at \"' + this.OPENVIDU_SERVER_URL +
                                '\"\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server' +
                                'is up and running at "' + this.OPENVIDU_SERVER_URL + '"')) {
                                location.assign(this.OPENVIDU_SERVER_URL + '/accept-certificate');
                            }
                        }
                        return observableThrowError(error);
                    })
                )
                .subscribe(response => {
                    resolve(response['id']);
                });
        });
    }

    // Function to discover cameras via Backend, not in use right now
    discoverCameras(sessionID) {
        const options = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa('OPENVIDUAPP:' + this.OPENVIDU_SERVER_SECRET),
                'Content-Type': 'text/plain'
            })
        };
        this.httpClient.post('https://localhost:8080/cameras/local', sessionID, options).subscribe(response => {
            console.log(response);
        });
    }

    addLocalCameras() {
        this.httpClient.get('https://localhost:8080/cameras/local', {responseType: 'text'}).subscribe(response => {
            console.log(response);
            console.log(JSON.parse(response));
            this.availableIpCameras = JSON.parse(response);
        })
    }

    deleteCamera(sessionID, cameraName) {
        const options = {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa('USER:' + 'Surveillance'),
                'Content-Type': 'application/json'
            })
        };
        console.log('Deleting ' + cameraName + ' - ' + sessionID);
        let url = 'https://localhost:8080/session/' + sessionID + '/cameras/' + cameraName;
        console.log(url);
        this.httpClient.delete('https://localhost:8080/session/' + sessionID + '/cameras/' + cameraName, options)
            .subscribe(next => {
                console.log(next)
            })
    }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {throwError as observableThrowError} from 'rxjs/internal/observable/throwError';
import {StreamManager} from 'openvidu-browser';
import {CameraDevice} from '../interfaces/camera-device';
import {AlertService} from '../_alert';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CameraService {
    private backendUrl = environment.OPENVIDUSURVEILLANCE_BACKEND_URL;
    public mainStreamManager: StreamManager;
    public availableIpCameras: CameraDevice[];
    public autoCloseAlert = {
        autoClose: true
    }
    public options = {
        headers: new HttpHeaders({
            'Authorization': 'Basic ' + sessionStorage.getItem('token'),
            'Content-type': 'application/x-www-form-urlencoded',
        })
    }

    constructor(public httpClient: HttpClient, protected alertService: AlertService) {
    }


    publishDemoCameras(sessionId: string) {
        return new Promise((resolve, reject) => {
            JSON.stringify({
                sessionName: sessionId
            });
            // return this.httpClient.post(this.OPENVIDU_SERVER_URL + '/api/sessions/' + sessionId + '/connection', body, options)

            return this.httpClient.get(this.backendUrl + 'session/' + sessionId + '/cameras/demo', this.options)
                .pipe(
                    catchError(error => {
                        if (error.status === 409) {
                            this.alertService.clear();
                            this.alertService.success('Demo cameras added correctly', this.autoCloseAlert);
                            resolve(sessionId);
                        } else if (error.status === 500) {
                            // alert('Error adding cameras: ' + JSON.parse(msg.error).message);
                            this.alertService.clear();
                            this.alertService.error('Error adding demo cameras, already exist in session', this.autoCloseAlert);
                            reject(error);
                        }
                        return observableThrowError(error.message);
                    })
                )
                .subscribe(() => {
                    this.alertService.clear();
                    this.alertService.success('Demo cameras added correctly', this.autoCloseAlert);
                });
        });
    }

    publishIpCamera(sessionId, rtspUri, cameraName, adaptativeBitrate, onlyPlayWhenSubscribers,
                    cameraIp?, port?, cameraUser?, cameraPassword?) {
        return new Promise((resolve, reject) => {
            console.log('Camera ' + rtspUri);
            let body;
            if (cameraIp && port) {
                body = JSON.stringify({
                    rtspUri: rtspUri, data: cameraName, adaptativeBitrate: adaptativeBitrate,
                    onlyPlayWithSubscribers: onlyPlayWhenSubscribers, cameraIp: cameraIp,
                    port: port, cameraUser: cameraUser, cameraPassword: cameraPassword
                });
            } else {
                body = JSON.stringify({
                    rtspUri: rtspUri, data: cameraName, adaptativeBitrate: adaptativeBitrate,
                    onlyPlayWithSubscribers: onlyPlayWhenSubscribers
                });
            }
            const options = {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + sessionStorage.getItem('token'),
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                })
            };
            return this.httpClient.post(this.backendUrl + 'session/' + sessionId + '/camera', body, options)
                .pipe(
                    catchError(error => {
                        if (error.status === 409) {
                            this.alertService.clear();
                            this.alertService.success('Camera added correctly', this.autoCloseAlert);
                            resolve(sessionId);
                        } else {
                            // alert('Error adding cameras: ' + JSON.parse(msg.error).message);
                            this.alertService.clear();
                            this.alertService.error(error.error.message, this.autoCloseAlert);
                            reject(error);
                        }
                        return observableThrowError(error.message);
                    })
                )
                .subscribe(response => {
                    this.alertService.clear();
                    this.alertService.success('Camera added correctly', this.autoCloseAlert);
                    resolve(response['id']);
                });
        });
    }

    // Function to discover cameras via Backend, not in use right now
    discoverCameras(sessionID) {
        this.availableIpCameras = null;
        let headers = new HttpHeaders({
            'Authorization': 'Basic ' + sessionStorage.getItem('token'),
            'Content-type': 'application/x-www-form-urlencoded',
            'Response-type': 'text'
        })
        this.httpClient.get(this.backendUrl + '' + sessionID + '/localCameras', {
            responseType: 'text',
            headers
        }).subscribe(response => {
            console.log(response);
            // this.addLocalCameras();
            if (response !== '[]') {
                this.availableIpCameras = JSON.parse(response);
            }
        });
    }

    deleteCamera(sessionID, cameraName) {
        console.log(environment.OPENVIDUSURVEILLANCE_BACKEND_URL)
        console.log('Deleting ' + cameraName + ' - ' + sessionID);
        let url = this.backendUrl + 'session/' + sessionID + '/cameras/' + cameraName;
        console.log(url);
        this.httpClient.delete(this.backendUrl + 'session/' + sessionID + '/cameras/' + cameraName, this.options)
            .subscribe(next => {
                console.log(next);
                this.mainStreamManager = null;
                this.alertService.success('Camera ' + cameraName + ' deleted');
            }, error => {
                console.log(error);
                this.alertService.error('Camera ' + cameraName + ' was not deleted');
            })
    }
}

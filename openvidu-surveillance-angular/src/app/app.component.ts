import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Component, HostListener, OnDestroy} from '@angular/core';
import {OpenVidu, Session, StreamEvent, StreamManager, Subscriber} from 'openvidu-browser';
import {CameraService} from './services/camera.service';
import {AlertService} from './_alert';
import {catchError} from 'rxjs/operators';
import {throwError as observableThrowError} from 'rxjs/internal/observable/throwError';
import {Observable} from 'rxjs';
import {environment} from '../environments/environment';


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {
    // Login
    logged: Boolean = false;
    userName: String;
    password: String;

    headerSettingsOn: Boolean = false;
    OPENVIDU_SERVER_SECRET = 'MY_SECRET';
    // OpenVidu objects
    OV: OpenVidu;
    session: Session;
    correctSession: Boolean = false;
    publisher: StreamManager; // Local
    subscribers: StreamManager[] = []; // Remotes
    // Join form
    mySessionId: string;
    // Variable to display "tutorial" message
    firstTime: Boolean = true;
    options = {
        autoClose: !this.firstTime
    }
    private backendUrl = environment.OPENVIDUSURVEILLANCE_BACKEND_URL;

    constructor(private httpClient: HttpClient,
                public cameraService: CameraService,
                public alertService: AlertService) {

    }

    toSettings() {
        this.headerSettingsOn = !this.headerSettingsOn
    }

    logOut(state: Boolean) {
        if (state === false) {
            this.session = null;
            this.logged = false;
        }
    }


    @HostListener('window:beforeunload')
    beforeunloadHandler() {
        // On window closed leave session
        this.leaveSession();
    }

    ngOnDestroy() {
        // On component destroyed leave session
        this.leaveSession();
    }

    authenticate() {
        this.httpClient.get<Observable<boolean>>(this.backendUrl + 'login', {
            headers: new HttpHeaders({
                'Authorization': 'Basic ' + btoa(this.userName + ':' + this.password),
                'Content-type': 'application/x-www-form-urlencoded',
            })
        })
            .pipe(
                catchError(error => {
                    if (error.status === 401) {
                        this.alertService.clear();
                        this.alertService.error('Authentication failed, check your username and/or password')
                    }
                    return observableThrowError(error.message);
                })
            ).subscribe(isValid => {
            if (isValid) {
                sessionStorage.setItem(
                    'token',
                    btoa(this.userName + ':' + this.password)
                );
                this.logged = true;
                this.alertService.success('Welcome ' + this.userName);
            } else {
                this.alertService.error('Username or password incorrect');
            }
        })
    }

    joinSession() {
        console.log('Sesion creada correctamente');
        // --- 1) Get an OpenVidu object ---
        this.OV = new OpenVidu();

        // --- 2) Init a session ---
        this.session = this.OV.initSession();

        // --- 3) Specify the actions when events take place in the session ---
        // On every new Stream received...
        this.session.on('streamCreated', (event: StreamEvent) => {
            // Subscribe to the Stream to receive it. Second parameter is undefined
            // so OpenVidu doesn't create an HTML video by its own
            let subscriber: Subscriber = this.session.subscribe(event.stream, undefined);
            this.subscribers.push(subscriber);
            this.firstTime = false;
        });
        // On every Stream destroyed...
        this.session.on('streamDestroyed', (event: StreamEvent) => {

            // Remove the stream from 'subscribers' array
            this.deleteSubscriber(event.stream.streamManager);
        });
        // --- 4) Connect to the session with a valid user token ---

        // 'getToken' method is simulating what your server-side should do.
        // 'token' parameter should be retrieved and returned by your own backend
        this.getToken().then(token => {

            // First param is the token got from OpenVidu Server. Second param can be retrieved by every user on event
            // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
            this.session.connect(token)
                .then(() => {
                    // The alert has autoclose only if the connected message displays
                    this.alertService.success('Connected', this.options);
                })
                .catch(error => {
                    this.alertService.error(error, this.options);
                    console.log('There was an error connecting to the session:', error.error, error.message);
                });
        });
    }

    leaveSession() {

        // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
        if (this.session) {
            this.session.disconnect();
        }
        // Empty all properties...
        this.mySessionId = '';
        this.subscribers = [];
        delete this.publisher;
        delete this.session;
        delete this.OV;
    }

    deleteSubscriber(streamManager: StreamManager):
        void {
        let index = this.subscribers.indexOf(streamManager, 0);
        if (index > -1
        ) {
            this.subscribers.splice(index, 1);
        }
    }

    getToken(): Promise<string> {
        return this.createSession();
    }

    createSession(): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            let opt = {
                autoClose: true
            }
            const options = {
                headers: new HttpHeaders({
                    'Authorization': 'Basic ' + sessionStorage.getItem('token'),
                    'Content-type': 'application/x-www-form-urlencoded',
                })
            }
            return this.httpClient.get(this.backendUrl + 'session/' + this.mySessionId, options)
                .pipe(
                    catchError(error => {
                        if (error.status === 202) {
                            resolve();
                        } else if (error.status === 500) {
                            this.alertService.clear();
                            this.alertService.error('There was an error with  your server', opt);
                            reject(error);
                        } else if (error.status === 400) {
                            this.alertService.clear();
                            this.alertService.error(error.error, opt);
                            console.log(error.error)
                            reject(error);
                        } else {
                            console.log(error)
                            console.warn('No connection to OpenVidu Server. ' +
                                'This may be a certificate error at ' + this.backendUrl);
                            if (window.confirm('No connection to OpenVidu Server. ' +
                                'This may be a certificate error at \"' + this.backendUrl +
                                '\"\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server' +
                                'is up and running at "' + this.backendUrl + '"')) {
                                location.assign(this.backendUrl + '/accept-certificate');
                            }
                        }
                        resolve();
                        return observableThrowError(error.message);
                    })
                )
                .subscribe((res) => {
                    this.correctSession = true;
                    console.log(res['token']);
                    resolve(res['token'])
                });
        });
    }
}

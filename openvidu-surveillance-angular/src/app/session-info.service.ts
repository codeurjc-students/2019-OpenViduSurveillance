import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Session} from './session';
import {Observable, of} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SessionInfoService {
    constructor(public httpClient: HttpClient) {

    }

    getSessionInfo(sessionId: string): Observable<string> {
        let url = 'https://localhost:8080/session/' + sessionId;
        return this.httpClient.get(url, {responseType: 'text'});
    }

}

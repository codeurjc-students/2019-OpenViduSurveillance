import {Component, Input, OnInit} from '@angular/core';
import {Session} from '../session';
import {SessionInfoService} from '../session-info.service';


@Component({
    selector: 'app-session-info',
    templateUrl: './session-info.component.html',
    styleUrls: ['./session-info.component.css']
})
export class SessionInfoComponent implements OnInit {

    @Input() sessionId: string;
    session: Session;

    constructor(public sessionInfoService: SessionInfoService) {
    }

    ngOnInit() {
       this.sessionInfoService.getSessionInfo(this.sessionId).subscribe(response => {
            this.session = JSON.parse(response)
       }, error => console.log(error));
    }

}

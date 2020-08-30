import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {StreamManager} from 'openvidu-browser';

@Component({
    selector: 'user-video',
    styles: [`
        ov-video {
            width: 100%;
            height: auto;
            float: left;
            cursor: pointer;
            text-align: center;
        }

        div div {
            margin: 0 auto;
            text-align: center;
            padding-left: 5px;
            padding-right: 5px;
            color: #0c1821;
            font-weight: bold;
            border-bottom-right-radius: 4px;
            width: fit-content;
            font-size: 20px;
        }

        p {
            margin: 0;
        }`],
    template: `
        <div>
            <div><p>{{cameraName}}</p></div>
            <ov-video [mainCamera]="mainCamera" [streamManager]="streamManager"></ov-video>
        </div>`
})
export class UserVideoComponent implements OnInit {

    @Input()
    streamManager: StreamManager;
    @Input()
    cameraName: string;
    @Input()
    mainCamera: Boolean;

    getNicknameTag() { // Gets the nickName of the user
        return JSON.parse(this.streamManager.stream.connection.data).clientData;
    }

    ngOnInit() {
        this.cameraName = this.streamManager.stream.connection.data;
    }
}

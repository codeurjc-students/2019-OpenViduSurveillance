import {AfterViewInit, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {StreamManager} from 'openvidu-browser';

@Component({
    selector: 'ov-video',
    template: `
        <div *ngIf="mainCamera; else mini">
            <video #videoElement poster="../assets/gifs/Spinner-XL.gif"></video>
        </div>
        <ng-template #mini>
            <video #videoElement poster="../assets/gifs/Spinner-M.gif"></video>
        </ng-template>
    `

})
export class OpenViduVideoComponent implements AfterViewInit {

    @ViewChild('videoElement', {static: false}) elementRef: ElementRef;

    _streamManager: StreamManager;

    @Input() mainCamera: Boolean;

    ngAfterViewInit() {
        this._streamManager.addVideoElement(this.elementRef.nativeElement);
    }

    @Input()
    set streamManager(streamManager: StreamManager) {
        this._streamManager = streamManager;
        if (!!this.elementRef) {
            this._streamManager.addVideoElement(this.elementRef.nativeElement);
        }
    }
}

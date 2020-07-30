import {Component, Input} from '@angular/core';
import {CameraService} from '../camera.service';
import {FormControl, FormGroup} from '@angular/forms';
import {CameraDevice} from '../camera-device';
import {StreamManager} from 'openvidu-browser';
import {MatDialog} from '@angular/material/dialog';
import {CloseDialogComponent} from '../header/header.component';

@Component({
    selector: 'app-demo-cameras',
    templateUrl: './administration-panel.component.html',
    styleUrls: ['./administration-panel.component.css']
})
export class AdministrationPanelComponent {
    devices: CameraDevice[];
    @Input() sessionId: string;
    @Input() subscribers: StreamManager[] = [];

    form = new FormGroup({
        devices: new FormControl(this.cameraService.availableIpCameras)
    })
    selectedDevice: CameraDevice;
    cameraToDelete: CameraDevice;
    hide = true;
    cameraName: String;
    cameraUrl: String

    constructor(public cameraService: CameraService, public dialog: MatDialog) {
        this.devices = cameraService.availableIpCameras;
    }

    addDemoCameras() {
        this.cameraService.publishDemoCameras(this.sessionId);
    }

    deleteDialogDisplay() {
        const dialog = this.dialog.open(DeleteDialogComponent);
        dialog.componentInstance.camToDelete = this.cameraToDelete;
    }
}

@Component({
    selector: 'close-dialog',
    template: `
        <h1 mat-dialog-title>Leaving session</h1>
        <div mat-dialog-content>¿Are you sure you want to delete "{{camToDelete}}" camera?</div>
        <div mat-dialog-actions>
            <button id="exitButton" mat-button mat-dialog-close>Yes</button>
            <button mat-button mat-dialog-close>No</button>
        </div>
    `,
    styleUrls: ['../header/header.component.css']
})
export class DeleteDialogComponent {
    @Input() camToDelete: CameraDevice;

}

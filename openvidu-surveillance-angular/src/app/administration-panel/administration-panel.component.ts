import {Component, Input} from '@angular/core';
import {CameraService} from '../services/camera.service';
import {FormControl, FormGroup} from '@angular/forms';
import {CameraDevice} from '../interfaces/camera-device';
import {StreamManager} from 'openvidu-browser';
import {MatDialog} from '@angular/material/dialog';
import {AlertService} from '../_alert';

@Component({
    selector: 'app-administration-panel',
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

    cameraToDelete: CameraDevice;

    cameraName: String;
    cameraUrl: String;
    cameraIp: String;
    cameraPort: Number;
    cameraUser: String;
    cameraPassword: String;
    displayedColumns: string[] = ['URL'];
    options = {
        autoClose: true
    }
    loading: Boolean = false;
    isChecked: Boolean;

    constructor(public cameraService: CameraService, public dialog: MatDialog,
                public alertService: AlertService) {
        this.devices = cameraService.availableIpCameras;
    }

    deleteDialogDisplay() {
        const dialog = this.dialog.open(DeleteDialogComponent);
        dialog.componentInstance.camToDelete = this.cameraToDelete;
        dialog.componentInstance.sessionId = this.sessionId;
        dialog.componentInstance.cameraName = this.cameraName;
    }
}

@Component({
    selector: 'close-dialog',
    template: `
        <h1 mat-dialog-title>Delete camera</h1>
        <div mat-dialog-content>¿Are you sure you want to delete "{{camToDelete}}" camera?</div>
        <div mat-dialog-actions>
            <button id="exitButton" mat-button mat-dialog-close (click)="deleteCam()">Yes</button>
            <button mat-button mat-dialog-close>No</button>
        </div>
    `,
    styleUrls: ['../header/header.component.css']
})
export class DeleteDialogComponent {
    @Input() camToDelete: CameraDevice;
    @Input() sessionId: string;
    @Input() cameraName: String;

    constructor(public cameraService: CameraService) {
    }

    deleteCam() {
        this.cameraService.deleteCamera(this.sessionId, this.camToDelete);
        this.camToDelete = null;
    }
}

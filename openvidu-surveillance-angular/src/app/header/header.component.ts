import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
    @Output() settingsOn = new EventEmitter<Boolean>();
    @Output() activeSession = new EventEmitter<Boolean>();
    @Input() session: Boolean;

    constructor(public dialog: MatDialog, public router: Router) {
    }

    ngOnInit() {
    }

    settings() {
        this.settingsOn.emit(true);
        console.log('Entering settings');
    }

    openDialog() {
        const dialog = this.dialog.open(CloseDialogComponent);
    }
}

@Component({
    selector: 'close-dialog',
    template: `
        <h1 mat-dialog-title>Leaving session</h1>
        <div mat-dialog-content>¿Are you sure you want to leave this session?</div>
        <div mat-dialog-actions>
            <a href="">
                <button id="exitButton" mat-button mat-dialog-close >Yes</button>
            </a>
            <button mat-button mat-dialog-close>No</button>
        </div>
    `,
    styleUrls: ['./header.component.css']
})
export class CloseDialogComponent {
}

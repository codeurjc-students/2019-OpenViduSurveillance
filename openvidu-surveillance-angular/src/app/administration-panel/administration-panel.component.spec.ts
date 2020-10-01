import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AdministrationPanelComponent} from './administration-panel.component';
import {MatCardModule} from '@angular/material/card';
import {MatListModule} from '@angular/material/list';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {AlertModule} from '../_alert';
import {MatTableModule} from '@angular/material/table';
import {ClipboardModule} from '@angular/cdk/clipboard';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';

describe('AdministrationPanelComponent', () => {
    let component: AdministrationPanelComponent;
    let fixture: ComponentFixture<AdministrationPanelComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AdministrationPanelComponent],
            imports: [
                MatCardModule,
                MatListModule,
                MatDialogModule,
                MatInputModule,
                BrowserModule,
                BrowserAnimationsModule,
                FormsModule,
                HttpClientModule,
                ReactiveFormsModule,
                MatSelectModule,
                MatIconModule,
                MatButtonModule,
                AlertModule,
                MatTableModule,
                ClipboardModule,
                MatSlideToggleModule
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AdministrationPanelComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should have h4 title', async(() => {
        expect(component.cameraService.availableIpCameras).toEqual(undefined);
    }));
});

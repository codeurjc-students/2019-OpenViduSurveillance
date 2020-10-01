import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {UserVideoComponent} from './user-video.component';
import {OpenViduVideoComponent} from './ov-video.component';
import {CloseDialogComponent, HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {SessionCamerasComponent} from './session-cameras/session-cameras.component';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';
import {AdministrationPanelComponent, DeleteDialogComponent} from './administration-panel/administration-panel.component';
import {MainCameraComponent} from './main-camera/main-camera.component';
import {MatSelectModule} from '@angular/material/select';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {APP_BASE_HREF} from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';

import {AlertModule} from './_alert';
import {MatTableModule} from '@angular/material/table';
import {ClipboardModule} from '@angular/cdk/clipboard';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';


@NgModule({
    declarations: [
        AppComponent,
        UserVideoComponent,
        OpenViduVideoComponent,
        HeaderComponent,
        FooterComponent,
        SessionCamerasComponent,
        AdministrationPanelComponent,
        MainCameraComponent,
        CloseDialogComponent,
        DeleteDialogComponent
    ],
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
    ],
    /*This line is needed for the popup dialog to work properly because of Ivy disabled*/
    entryComponents: [CloseDialogComponent, DeleteDialogComponent],
    providers: [{provide: APP_BASE_HREF, useValue: '/'}],
    bootstrap: [AppComponent]
})
export class AppModule {
}

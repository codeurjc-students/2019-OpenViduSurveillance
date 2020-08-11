import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {UserVideoComponent} from './user-video.component';
import {OpenViduVideoComponent} from './ov-video.component';
import {CloseDialogComponent, HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {SessionInfoComponent} from './session-info/session-info.component';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';
import {AdministrationPanelComponent, DeleteDialogComponent} from './administration-panel/administration-panel.component';
import { MainCameraComponent } from './main-camera/main-camera.component';
import {MatSelectModule} from '@angular/material/select';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import {APP_BASE_HREF} from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';


@NgModule({
    declarations: [
        AppComponent,
        UserVideoComponent,
        OpenViduVideoComponent,
        HeaderComponent,
        FooterComponent,
        SessionInfoComponent,
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
        AppRoutingModule,
        MatIconModule,
        MatButtonModule
    ],
    /*This line is needed for the popup dialog to work properly because of Ivy disabled*/
    entryComponents: [CloseDialogComponent, DeleteDialogComponent],
    providers: [{provide: APP_BASE_HREF, useValue: '/'}],
    bootstrap: [AppComponent]
})
export class AppModule {
}

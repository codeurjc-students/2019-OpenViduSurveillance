import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {UserVideoComponent} from './user-video.component';
import {OpenViduVideoComponent} from './ov-video.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {SessionInfoComponent} from './session-info/session-info.component';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';
import { AdministrationPanelComponent } from './administration-panel/administration-panel.component';
import { MainCameraComponent } from './main-camera/main-camera.component';
import {MatSelectModule} from '@angular/material/select';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


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
    ],
    imports: [
        MatCardModule,
        MatListModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatSelectModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}

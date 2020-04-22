import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {UserVideoComponent} from './user-video.component';
import {OpenViduVideoComponent} from './ov-video.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {SessionInfoComponent} from './session-info/session-info.component';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';
import { DemoCamerasComponent } from './demo-cameras/demo-cameras.component';
import { MainCameraComponent } from './main-camera/main-camera.component';


@NgModule({
    declarations: [
        AppComponent,
        UserVideoComponent,
        OpenViduVideoComponent,
        HeaderComponent,
        FooterComponent,
        SessionInfoComponent,
        DemoCamerasComponent,
        MainCameraComponent,
    ],
    imports: [
        MatCardModule,
        MatListModule,
        BrowserModule,
        FormsModule,
        HttpClientModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}

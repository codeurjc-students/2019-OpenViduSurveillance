import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AdministrationPanelComponent} from './administration-panel/administration-panel.component';
import {MainCameraComponent} from './main-camera/main-camera.component';

const routes: Routes = [
  { path: '', redirectTo: '/main', pathMatch: 'full' },
  { path: 'main', component: MainCameraComponent },
  { path: 'configuration', component: AdministrationPanelComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

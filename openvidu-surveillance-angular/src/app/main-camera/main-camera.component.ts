import {Component, Input, OnInit} from '@angular/core';
import {StreamManager} from 'openvidu-browser';
import {CameraService} from '../camera.service';

@Component({
  selector: 'app-main-camera',
  templateUrl: './main-camera.component.html',
  styleUrls: ['./main-camera.component.css']
})
export class MainCameraComponent implements OnInit {
  @Input() mainStreamManager: StreamManager;

  constructor(private cameraService: CameraService) { }

  ngOnInit() {
  }

}

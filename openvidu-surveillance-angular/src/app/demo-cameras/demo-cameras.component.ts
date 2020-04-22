import {Component, Input, OnInit} from '@angular/core';
import {CameraService} from '../camera.service';

@Component({
  selector: 'app-demo-cameras',
  templateUrl: './demo-cameras.component.html',
  styleUrls: ['./demo-cameras.component.css']
})
export class DemoCamerasComponent implements OnInit {
  @Input() sessionId: string;
  constructor(private cameraService: CameraService) { }

  ngOnInit() {
  }

  addDemoCameras() {
    this.cameraService.publishDemoCameras(this.sessionId);
  }
}

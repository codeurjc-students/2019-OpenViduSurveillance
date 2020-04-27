import {Component, Input, OnInit} from '@angular/core';
import {CameraService} from '../camera.service';
// import { onvifDevices$ } from 'camera-probe'

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

  // discoverCameras() {
  //   const subscription = onvifDevices$().subscribe(console.log)
  // }
//   discoverCameras() {
//     const onvif = require('node-onvif');
//
//     console.log('Start the discovery process.');
// // Find the ONVIF network cameras.
// // It will take about 3 seconds.
//     onvif.startProbe().then((device_info_list) => {
//       console.log(device_info_list.length + ' devices were found.');
// // Show the device name and the URL of the end point.
//       device_info_list.forEach((info) => {
//         console.log('- ' + info.urn);
//         console.log('  - ' + info.name);
//         console.log('  - ' + info.xaddrs[0]);
//       });
//     }).catch((error) => {
//       console.error(error);
//     });
//   }
}

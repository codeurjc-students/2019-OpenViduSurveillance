import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Session} from 'openvidu-browser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Output() settingsOn = new EventEmitter<Boolean>();
  @Input() session: Boolean;

  constructor() { }

  ngOnInit() {
  }

  settings() {
    this.settingsOn.emit(true);
    console.log('Entering settings')
  }
}

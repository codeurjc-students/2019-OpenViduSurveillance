import {Component, Input, OnInit} from '@angular/core';
import {Session} from 'openvidu-browser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() session: Boolean;

  constructor() { }

  ngOnInit() {
  }

}

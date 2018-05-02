import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import { Location } from '@angular/common';

@Component({
  selector: 'location-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  @Input() latitude : number = 0;
  @Input() longitude : number = 0;

  constructor(private route: ActivatedRoute, private location: Location) { }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      this.latitude = +params.get("latitude");
      this.longitude = +params.get("longitude");
    });
  }

  cancel() {
    this.location.back();
  }

}

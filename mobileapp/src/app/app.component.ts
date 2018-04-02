import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
declare let device;
import {appMenus} from "./app.menu";
import {AuthenticationService} from "./auth/service/authentication.service";
import {User} from "./auth/auth.model";
import {MatSidenav} from "@angular/material";
import {isNullOrUndefined} from "util";
import {DeviceDetectorService} from "ngx-device-detector";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{

  // drawerMenu;
  @ViewChild('sidenav') sidenav : MatSidenav;
  title = 'FUTBOLDESA';
  menus = appMenus;
  user = new User();
  device;
  isWeb : boolean = true;
  deviceInfo;

  constructor(private authService: AuthenticationService,
              private changeDetector: ChangeDetectorRef,
              private deviceService: DeviceDetectorService,
  ) {

  }

  ngOnInit(): void {
    this.deviceInfo = this.deviceService.getDeviceInfo();

    this.authService.ping(); //redirects to login if user credentials fail

    this.setUser();

    this.initMobile();

    if(!isNullOrUndefined(device)) this.isWeb = true;

  }

  initMobile(){
    document.addEventListener("deviceready", function() {
      alert(device.platform);
    }, false);
  }

  onSidenavToggle(){
    this.sidenav.toggle();
    if(this.sidenav.opened) this.populateSidenav();
  }

  populateSidenav(){
    this.setUser();
  }

  setUser(){
    this.authService.findCurrentUser().subscribe(
      data => {
          this.user = data;
          this.changeDetector.markForCheck();
      },
      error => console.log(error)
    );
  }
}

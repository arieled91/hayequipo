import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {adminMenus, publicMenus} from "./app.menu";
import {Privileges, User} from "./auth/auth.model";
import {MatSidenav} from "@angular/material";
import {DeviceDetectorService} from "ngx-device-detector";
import {AuthService} from "./auth/service/auth.service";

declare let device;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterViewInit{

  // drawerMenu;
  @ViewChild('sidenav') sidenav : MatSidenav;
  title = 'FUTBOLDESA';
  publicMenus = publicMenus;
  adminMenus = adminMenus;
  user : any = new User();
  device;
  deviceInfo;
  privileges : string[] = [];
  Privileges = Privileges;

  constructor(private changeDetector: ChangeDetectorRef,
              private deviceService: DeviceDetectorService,
              private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.deviceInfo = this.deviceService.getDeviceInfo();
    this.initMobile();
    this.authService.getUserPrivileges().subscribe(data=>{
      this.privileges = data;
      console.log(this.privileges.includes(Privileges.FULL_ACCESS));
    });
  }

  isWeb(){
    return document.location.protocol == "http:" || document.location.protocol == "https:";
  }

  initMobile(){
    if (!this.isWeb()) {
      document.addEventListener("deviceready", function() {
        alert(device.platform);
      }, false);
    }

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
      error => console.log("currentusererror "+error)
    );
  }

  ngAfterViewInit() {
  }
}

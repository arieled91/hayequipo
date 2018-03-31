import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {appMenus} from "./app.menu";
import {AuthenticationService} from "./auth/service/authentication.service";
import {User} from "./auth/auth.model";
import {MatSidenav} from "@angular/material";

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


  constructor(private authService: AuthenticationService, private changeDetector: ChangeDetectorRef) {

  }



  ngOnInit(): void {
    this.authService.ping(); //redirects to login if user credentials fail
    this.setUser();
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

import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {appMenus} from "./app.menu";
import {AuthenticationService} from "./auth/service/authentication.service";
import {User} from "./auth/auth.model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{

  // drawerMenu;

  title = 'FUTBOLDESA';
  menus = appMenus;
  user = new User();


  constructor(private authService: AuthenticationService, private changeDetector: ChangeDetectorRef) {

  }



  ngOnInit(): void {
    this.authService.ping(); //redirects to login if user credentials fail
    this.setUser();
  }

  // openDrawerMenu(){
  //   this.setUser();
  //   this.drawerMenu.open = true;
  // }
  //
  // closeDrawerMenu(){
  //   this.setUser();
  //   this.drawerMenu.open = false;
  // }

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

import {AfterContentInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {appMenus} from "./app.menu";
import {MDCTemporaryDrawer} from '@material/drawer';
import {AuthenticationService} from "./auth/service/authentication.service";
import {User} from "./auth/auth.interfaces";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterContentInit, OnInit{

  drawerMenu;

  title = 'FUTBOLDESA';
  menus = appMenus;
  user = new User();

  @ViewChild('drawerMenuRef') drawerMenuRef: ElementRef;

  constructor(private authService: AuthenticationService) {

  }

  ngAfterContentInit(): void {
    this.drawerMenu = new MDCTemporaryDrawer(this.drawerMenuRef.nativeElement);
  }

  ngOnInit(): void {
    this.authService.ping(); //redirects to login if user credentials fail
    this.setUser();
  }

  openDrawerMenu(){
    this.drawerMenu.open = true;
  }

  userName(){
    return `${this.user.firstName} ${this.user.lastName}`
  }

  setUser(){
    this.authService.findCurrentUser().subscribe(
      data => {this.user = data},
      error => console.log(error)
    );
  }
}

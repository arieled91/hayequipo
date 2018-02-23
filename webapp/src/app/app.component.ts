import {AfterContentInit, Component, ElementRef, ViewChild} from '@angular/core';
import {appMenus} from "./app.menu";
import {MDCTemporaryDrawer} from '@material/drawer';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements AfterContentInit{
  drawerMenu;
  active = 0;

  title = 'FUTBOLDESA';
  menus = appMenus;

  @ViewChild('drawerMenuRef') drawerMenuRef: ElementRef;

  constructor() {}

  ngAfterContentInit(): void {
    this.drawerMenu = new MDCTemporaryDrawer(this.drawerMenuRef.nativeElement);
  }

  activate(index: number) {
    this.active = index;
  }

  openDrawerMenu(){
    this.drawerMenu.open = true;
  }
}

import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule} from "@angular/router";


import {AppComponent} from './app.component';
import {GameSearchComponent} from './game/game-search.component';
import {FormsModule} from "@angular/forms";
import { GameListComponent } from './game/game-list/game-list.component';


@NgModule({
  declarations: [
    AppComponent,
    GameSearchComponent,
    GameListComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]),
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

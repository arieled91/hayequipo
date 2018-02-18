import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule} from "@angular/router";


import {AppComponent} from './app.component';
import {GameSearchComponent} from './game/game-search.component';
import {FormsModule} from "@angular/forms";
import {GameListComponent} from './game/game-list/game-list.component';
import {HttpClientModule} from '@angular/common/http';
import {GameService} from './game/service/game.service';
import {HomeComponent} from './home/home/home.component';
import {AppRoutingModule} from "./app-routing.module";


@NgModule({
  declarations: [
    AppComponent,
    GameSearchComponent,
    GameListComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]),
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    GameService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

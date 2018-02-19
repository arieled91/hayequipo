import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule} from "@angular/router";


import {AppComponent} from './app.component';
import {GameSearchComponent} from './game/game-search.component';
import {FormsModule} from "@angular/forms";
import {GameListComponent} from './game/game-list/game-list.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {GameService} from './game/service/game.service';
import {HomeComponent} from './home/home/home.component';
import {AppRoutingModule} from "./app-routing.module";
import {LoginComponent} from './auth/login/login.component';
import {AuthenticationService} from "./service/authentication.service";
import {ApiHttpInterceptor} from "./service/api.interceptor";


@NgModule({
  declarations: [
    AppComponent,
    GameSearchComponent,
    GameListComponent,
    HomeComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]),
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [
    GameService,
    AuthenticationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiHttpInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

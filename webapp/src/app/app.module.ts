import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule} from "@angular/router";
import {FormsModule} from '@angular/forms';
import {MaterialModule} from '@blox/material';


import {AppComponent} from './app.component';
import {GameListComponent} from './game/game-list/game-list.component';
import {GameService} from './game/service/game.service';
import {HomeComponent} from './home/home/home.component';
import {AppRoutingModule} from "./app-routing.module";
import {LoginComponent} from './auth/login/login.component';
import {AuthenticationService} from "./service/authentication.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ApiHttpInterceptor} from "./service/api.interceptor";
import {GameFormComponent} from './game/game-form/game-form.component';
import {GameComponent} from "./game/game.component";
import {GameDialogComponent} from "./game/game-dialog.component";

import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MAT_DATE_LOCALE, MatInputModule, MatNativeDateModule} from "@angular/material";
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [
    AppComponent,
    GameComponent,
    GameListComponent,
    HomeComponent,
    LoginComponent,
    GameFormComponent,
    GameDialogComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([]),
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    MaterialModule,

    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatMomentDateModule
  ],
  providers: [
    GameService,
    AuthenticationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiHttpInterceptor,
      multi: true
    },
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'es-AR'
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

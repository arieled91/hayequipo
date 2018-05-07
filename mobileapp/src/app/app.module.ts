import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MaterialModule} from '@blox/material';

import {AppComponent} from './app.component';
import {GameListComponent} from './game/game-list/game-list.component';
import {GameService} from './game/service/game.service';
import {HomeComponent} from './home/home/home.component';
import {AppRoutingModule} from "./app-routing.module";
import {LoginComponent} from './auth/login/login.component';
import {GameFormComponent} from './game/game-form/game-form.component';
import {GameComponent, GameDialogComponent, PlayersDialogComponent} from "./game/game.component";

import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {MatFormFieldModule} from '@angular/material/form-field';
import {
  MAT_DATE_LOCALE,
  MatChipsModule,
  MatExpansionModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatNativeDateModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatTabsModule,
  MatToolbarModule,
} from "@angular/material";
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import {PlayerListComponent} from './game/player-list/player-list.component';
import {MapComponent} from "./map/map.component";
import {DeviceDetectorModule} from 'ngx-device-detector';
import {OAuthModule} from 'angular-oauth2-oidc';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ApiHttpInterceptor} from "./service/api.interceptor";
import {AuthService} from "./auth/service/auth.service";
import {AgmCoreModule} from "@agm/core";
import {GameCardComponent} from './game/game-card/game-card.component';
import {GameMapComponent} from './game/game-map/game-map.component';
import {ConfirmDialogComponent} from './common/dialog/confirm-dialog.component';
import {UserListComponent} from './auth/user-list/user-list.component';

@NgModule({
  declarations: [
    AppComponent,
    GameComponent,
    GameListComponent,
    HomeComponent,
    LoginComponent,
    GameFormComponent,
    GameDialogComponent,
    PlayersDialogComponent,
    PlayerListComponent,
    MapComponent,
    GameCardComponent,
    GameMapComponent,
    ConfirmDialogComponent,
    UserListComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    // RouterModule.forRoot([], { useHash: true }),
    FormsModule,
    HttpClientModule,
    DeviceDetectorModule.forRoot(),
    OAuthModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyBzWGn5njSvYpzsIJrJQUp9zU7jVG9unD8',
      libraries: ["places"]
    }),
    MaterialModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatMomentDateModule,
    MatToolbarModule,
    MatIconModule,
    MatTabsModule,
    MatGridListModule,
    MatListModule,
    MatSnackBarModule,
    MatSelectModule,
    MatChipsModule,
    MatSidenavModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
  ],
  providers: [
    GameService,
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiHttpInterceptor,
      multi: true,
    },
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'es-AR',
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

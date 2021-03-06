import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home/home.component";
import {GameComponent, GameDialogComponent, PlayersDialogComponent} from "./game/game.component";
import {LoginComponent} from "./auth/login/login.component";
import {GameFormComponent} from "./game/game-form/game-form.component";
import {GameCardComponent} from "./game/game-card/game-card.component";
import {MapComponent} from "./map/map.component";
import {GameMapComponent} from "./game/game-map/game-map.component";
import {ConfirmDialogComponent} from "./common/dialog/confirm-dialog.component";
import {UserListComponent} from "./auth/user-list/user-list.component";
import {RegisterComponent} from "./auth/register/register.component";
const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home',  component: HomeComponent },
  { path: 'game',  component: GameComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'game-form/:id', component: GameFormComponent },
  { path: 'game-form', component: GameFormComponent },
  { path: 'game-card', component: GameCardComponent },
  { path: 'game-dialog', component: ConfirmDialogComponent },
  { path: 'confirm-dialog', component: GameDialogComponent },
  { path: 'players-dialog', component: PlayersDialogComponent },
  { path: 'map', component: MapComponent },
  { path: 'game-map/:id', component: GameMapComponent },
  { path: 'user-list', component: UserListComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { useHash: true, onSameUrlNavigation: 'reload' }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}

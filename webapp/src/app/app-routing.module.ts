import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home/home.component";
import {GameSearchComponent} from "./game/game-search.component";
import {LoginComponent} from "./auth/login/login.component";
import {GameFormComponent} from "./game/game-form/game-form.component";
const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home',  component: HomeComponent },
  { path: 'game',  component: GameSearchComponent },
  { path: 'login', component: LoginComponent },
  { path: 'game-form', component: GameFormComponent }
];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}

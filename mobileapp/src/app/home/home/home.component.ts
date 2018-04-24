import { Component, OnInit } from '@angular/core';
import {GameService} from "../../game/service/game.service";
import {HttpClient, HttpHandler, HttpHeaders} from "@angular/common/http";
import {Game} from "../../game/game.model";
import {AuthService} from "../../auth/service/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  games = <any>[];

  constructor(private gameService: GameService, private http: HttpClient) { }

  ngOnInit() {
    this.populate();
  }

  populate(){
    this.gameService.findNext().subscribe(
      data => this.games = data
    );
  }

  // populate(){
  //   let headers = new HttpHeaders();
  //   // headers = headers.set('Content-Type', 'application/json; charset=utf-8');
  //   headers = headers.set('Session-Token',  localStorage.getItem(AuthService.SESSION_ID_KEY));
  //   this.http.get<Game>( 'http://localhost:8888/api/controller/games', {headers : headers} ).subscribe(
  //       data => this.games = data
  //     );
  // }

  reload(){
    this.populate()
  }

}

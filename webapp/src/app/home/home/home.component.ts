import { Component, OnInit } from '@angular/core';
import {GameService} from "../../game/service/game.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  games = [];

  constructor(private gameService: GameService) { }

  ngOnInit() {
    this.gameService.findNext().subscribe(
      data => this.games = data
    );
  }

}

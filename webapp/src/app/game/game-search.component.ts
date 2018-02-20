import {Component, OnInit} from '@angular/core';
import {GameService} from "./service/game.service";

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.css']
})
export class GameSearchComponent implements OnInit{

  title;
  gameDate;
  games;

  constructor(private gameService: GameService) {
    this.title = "Partidos";
    this.gameDate = null;
    this.games = [];
  }

  find() {
    this.gameService.findByDate(this.gameDate).subscribe(
      data => this.games = data
    );
    console.log(this.games);
  }
  ngOnInit(): void {
    this.find()
  }

}

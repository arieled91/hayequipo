import {Component} from '@angular/core';
import {GameService} from "./service/game.service";

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.css']
})
export class GameSearchComponent{

  gameDate;
  games;

  constructor(private gameService: GameService) {
    this.gameDate = new Date();
    this.games = [];
  }

  find(event: Event) {
    this.gameService.findByDate(this.gameDate).subscribe(
      data => this.games = data
    );
    console.log(this.games);
  }

}

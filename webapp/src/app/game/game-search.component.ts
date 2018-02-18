import {Component} from '@angular/core';
import {Game} from "./interfaces";
import {GameService} from "./service/game.service";

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.css']
})
export class GameSearchComponent{

  games: Array<Game>;

  constructor(private gameService: GameService) {
  }

  find(date: Date) {
    this.gameService.findByDate(date);
  }

}

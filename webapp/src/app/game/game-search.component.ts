import {Component, OnInit} from '@angular/core';
import {GameService} from "./service/game.service";

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.scss']
})
export class GameSearchComponent implements OnInit{

  title = "Partidos";
  dateLabel = "Buscar";

  gameDate = null;
  games = [];

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.find()
  }

  find() {
    this.gameService.findByDate(this.gameDate).subscribe(
      data => this.games = data
    );
    console.log(this.games);
  }


}

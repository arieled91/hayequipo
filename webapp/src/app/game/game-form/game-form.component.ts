import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../game.model";
import {isNullOrUndefined, isUndefined} from "util";
import {GameService} from "../service/game.service";

@Component({
  selector: 'app-game-form',
  templateUrl: './game-form.component.html',
  styleUrls: ['./game-form.component.scss']
})
export class GameFormComponent implements OnInit {

  title = "Partido";

  descriptionLabel = "Descripción";
  dateTimeLabel = "Fecha";
  locationLabel = "Lugar";

  @Input() game : Game;


  constructor(private gameService: GameService) {
  }

  ngOnInit() {
    if(isUndefined(this.game)) this.game = new Game()
  }

  saveGame() {
    if(isNullOrUndefined(this.game.id)) this.addNewGame();
  }

  addNewGame(){
    this.gameService.addNewGame(this.game).subscribe(
      data => this.game = data
    );
  }
}

import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../game.interfaces";
import {isUndefined} from "util";

@Component({
  selector: 'app-game-form',
  templateUrl: './game-form.component.html',
  styleUrls: ['./game-form.component.scss']
})
export class GameFormComponent implements OnInit {

  title = "Partido";
  descriptionLabel = "Descripción";

  @Input() game : Game;
  dateTimeLabel = "Fecha";


  constructor() {
  }

  ngOnInit() {
    if(isUndefined(this.game)) this.game = new Game()
  }

}

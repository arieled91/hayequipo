import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game} from "../game.model";
import {isNullOrUndefined} from "util";
import {GameService} from "../service/game.service";
import moment = require("moment");

@Component({
  selector: 'app-game-form',
  templateUrl: './game-form.component.html',
  styleUrls: ['./game-form.component.scss']
})
export class GameFormComponent implements OnInit {

  @Input() id : Number = null;
  title = "Partido";

  descriptionLabel = "Descripción";
  dateLabel = "Fecha";
  timeLabel = "Hora";
  locationDescLabel = "Lugar";
  locationAddrLabel = "Dirección";
  capacityLabel = "Capacidad";
  game : Game = new Game();

  @Output() onSaved = new EventEmitter<boolean>();
  @Output() onCancel = new EventEmitter<void>();

  constructor(private gameService: GameService) {
  }

  ngOnInit() {
    if(!isNullOrUndefined(this.id)) this.gameService.findById(this.id).subscribe(data => this.game = data);
  }

  save() {
    this.gameService.saveGame(this.game).subscribe(
      data => this.onSaved.emit(true)
    );
  }

  cancel(){
    this.onCancel.emit()
  }

  setTime(event){
    let time = event;
    this.game.dateTime = moment(this.game.dateTime).hours(isNullOrUndefined(time) ? 0 : event.substring(0,2));
    this.game.dateTime = moment(this.game.dateTime).minutes(isNullOrUndefined(time) ? 0 : event.substring(3,5));
    this.game.dateTime = moment(this.game.dateTime).seconds(0);
    this.game.dateTime = moment(this.game.dateTime).milliseconds(0);

    console.log(this.game.dateTime);
  }

  getGameTime(){
    return isNullOrUndefined(this.game.dateTime) ? this.game.dateTime : moment(this.game.dateTime).format("HH:mm")
  }

}

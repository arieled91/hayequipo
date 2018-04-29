import {Component, Input, OnInit} from '@angular/core';
import {GameService} from "../service/game.service";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent implements OnInit {

  @Input() game = null;
  players = [];
  reservePlayers = [];
  playersLabel = "Titulares";
  reservePlayersLabel = "Suplentes";

  constructor(private gameService: GameService) { }

  ngOnInit() {
    if(!isNullOrUndefined(this.game)) {
      this.players = this.game.players.slice(0, this.game.capacity);
      this.reservePlayers = this.game.players.slice(this.game.capacity, this.game.players.length);
    }
  }
}

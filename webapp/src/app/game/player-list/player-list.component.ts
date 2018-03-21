import {Component, Input, OnInit} from '@angular/core';
import {isNullOrUndefined} from "util";
import {GameService} from "../service/game.service";

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent implements OnInit {

  @Input() gameId : Number = null;
  game;
  players = [];
  reservePlayers = [];
  playersLabel = "Titulares";
  reservePlayersLabel = "Suplentes";

  constructor(private gameService: GameService) { }

  ngOnInit() {
    if(!isNullOrUndefined(this.gameId)) {
      this.gameService.findById(this.gameId).subscribe(data => this.game = data);
      this.gameService.listPlayers(this.gameId).subscribe(data => {
        this.players = data.slice(0, this.game.capacity);
        this.reservePlayers = data.slice(this.game.capacity, data.length);
      });
    }
  }

}

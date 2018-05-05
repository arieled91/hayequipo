import {Component, Input, OnInit} from '@angular/core';
import {isNullOrUndefined} from "util";
import {GameService} from "../service/game.service";
import {Game} from "../game.model";

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent implements OnInit {

  @Input() gameId : Number = null;
  game = null;
  players = [];
  reservePlayers = [];
  playersLabel = "Titulares";
  reservePlayersLabel = "Suplentes";
  loading = true;

  constructor(private gameService: GameService) { }

  ngOnInit() {
    if(!isNullOrUndefined(this.gameId)) {
      this.gameService.findById(this.gameId).subscribe(
        data => {
          this.populate(data);
          this.loading = false;
        }
      );
    }
  }

  populate(game: Game){
    this.game = game;
    this.gameService.listPlayers(this.game.id).subscribe(data => {
      this.players = data.slice(0, game.capacity);
      this.reservePlayers = data.slice(this.game.capacity, data.length);
    });
  }

}

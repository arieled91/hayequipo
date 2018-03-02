import {Component, Input, OnInit} from '@angular/core';
import {isNullOrUndefined} from "util";
import {GameService} from "../service/game.service";

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent implements OnInit {

  @Input() id : Number = null;
  players;

  constructor(private gameService: GameService) { }

  ngOnInit() {
    if(!isNullOrUndefined(this.id)) this.gameService.listPlayers(this.id).subscribe(data => this.players = data);
  }

}

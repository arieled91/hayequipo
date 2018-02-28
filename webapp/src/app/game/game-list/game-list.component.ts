import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../game.model";
import {GameService} from "../service/game.service";

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss']
})
export class GameListComponent implements OnInit {

  @Input() games : Game[];

  constructor(private gameService: GameService) {
  }

  ngOnInit() {}

  joinGame(id) {
    this.gameService.joinGame(id).subscribe(
      data => this.games.find(game => game.id == id).currentUserJoined = true
    )
  }

  exitGame(id){
    this.gameService.exitGame(id).subscribe(
      data => this.games.find(game => game.id == id).currentUserJoined = false
    )
  }
}

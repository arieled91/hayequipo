import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../game.model";
import {GameService} from "../service/game.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-game-map',
  templateUrl: './game-map.component.html',
  styleUrls: ['./game-map.component.scss']
})
export class GameMapComponent implements OnInit {

  @Input() gameId : number;
  game : Game = new Game();

  constructor(private route: ActivatedRoute, private gameService : GameService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.gameId = +params['id'];
      this.populate();
    });

  }

  populate(){
    this.gameService.findById(this.gameId).subscribe(data => this.game = data);
  }
}

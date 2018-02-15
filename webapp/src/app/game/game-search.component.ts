import { Component, OnInit } from '@angular/core';
import {Game} from "./interfaces";

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.css']
})
export class GameSearchComponent implements OnInit {

  games: Array<Game>;

  constructor() { }

  ngOnInit() {
  }

}

import {AfterContentInit, Component, OnInit} from '@angular/core';
import {GameService} from "./service/game.service";
import {MDCDialog} from '@material/dialog';
import {MatDialog} from "@angular/material";
import {GameDialogComponent} from "./game-dialog.component";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, AfterContentInit{


  title = "Partidos";
  dateLabel = "Filtrar Fecha";

  gameDialog;

  gameDate;
  games = [];

  newGame;


  constructor(private gameService: GameService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.find()
  }
  ngAfterContentInit(): void {
  }

  find() {
    this.gameService.findByDate(this.gameDate).subscribe(
      data => this.games = data
    );
  }


  openGameDialog() {
    let dialogRef = this.dialog.open(GameDialogComponent, {
      minWidth: '50%',
      minHeight: '50%',
      data: {}
    });

    dialogRef.afterClosed().subscribe(data => {
      if(data.reload)this.find();
    });
  }

  onGameDialogClose(refresh: boolean){
    if(refresh) this.find()
  }
}

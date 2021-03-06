import {Component, Inject, Input, OnInit} from '@angular/core';
import {GameService} from "./service/game.service";
import {MDCDialog} from '@material/dialog';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatSnackBar} from "@angular/material";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit{


  title = "Partidos";
  dateLabel = "Filtrar Fecha";

  gameDate;
  games = [];


  constructor(private gameService: GameService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.find();
  }

  find() {
    if(isNullOrUndefined(this.gameDate)) this.findAvailable();
    else this.findByDate();
  }

  findAvailable() {
    this.gameService.findAvailable().subscribe(
      data => this.games = data
    );
  }

  findByDate() {
    this.gameService.findByDate(this.gameDate).subscribe(
      data => this.games = data._embedded.games
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


@Component({
  selector: 'game-dialog',
  template:`
    <mat-dialog-content style="padding-bottom: 5px">
        <app-game-form [id]="id" (onSaved)="onSaved($event)" (onCancel)="onCancel()"></app-game-form>
    </mat-dialog-content>`
})
export class GameDialogComponent {

  @Input() public id : Number;

  constructor(
    private dialogRef: MatDialogRef<GameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data,
    private snackBar: MatSnackBar
  ) {
    this.id = data.id;
  }

  onSaved(close: boolean){
    if(close) {
      this.dialogRef.close({reload : true});
      this.snackBar.open('Guardado', '',{duration: 1000});
    }
  }
  onCancel(){
    this.dialogRef.close({reload : false});
    this.snackBar.open('Cancelado','',{duration: 1000});
  }
}

@Component({
  selector: 'players-dialog',
  template: '<app-player-list [gameId]="id"></app-player-list>',
})
export class PlayersDialogComponent {

  @Input() public id : Number;

  constructor(
    private dialogRef: MatDialogRef<GameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data,
    private snackBar: MatSnackBar
  ) {
    this.id = data.id;
  }
}

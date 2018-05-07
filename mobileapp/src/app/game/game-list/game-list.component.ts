import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game, GameStatus} from "../game.model";
import {GameService} from "../service/game.service";
import {MatDialog, MatSnackBar} from "@angular/material";
import {PlayersDialogComponent} from "../game.component";
import {buildMapQueryByAddress} from "../../map/googlemaps.util";
import {ConfirmDialogComponent} from "../../common/dialog/confirm-dialog.component";

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss']
})
export class GameListComponent implements OnInit {

  @Input() games : Game[];
  @Input() allowEdit : boolean = true;
  @Output() onDialogClose = new EventEmitter<boolean>();

  joinedGameLabel = "Anotado ¡Nos vemos en la cancha!";
  exitGameLabel = "¡Qué lastima! Será la próxima";
  confirmExitGameLabel = "¿Estás seguro que querés salir?";
  confirmNavigateToMapLabel = "¿Estás seguro que querés abrir el mapa?";
  cancelBtn = "Salir";
  Status = GameStatus;
  locale = "es";

  constructor(private gameService: GameService, private dialog: MatDialog, private snackBar: MatSnackBar) {
  }

  ngOnInit() {}

  joinGame(id, event) {
    event.stopPropagation();
    this.gameService.joinGame(id).subscribe(
      () => {
        this.games.find(game => game.id == id).currentUserJoined = true;
        this.snackBar.open(this.joinedGameLabel,'',{duration: 4000});
      }
    )
  }

  openPlayersDialog(id: Number) {
    this.dialog.open(PlayersDialogComponent, {
      minWidth: '100px',
      minHeight: '100px',
      data: {id : id}
    });
  }


  openMapConfirmDialog(game:Game){
    this.dialog.open(ConfirmDialogComponent, {
      data: {content : this.confirmNavigateToMapLabel, openUrl: buildMapQueryByAddress(game.location.address)}
    });
  }

  openExitGameConfirmDialog(game:Game){
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {content : this.confirmExitGameLabel}
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if(confirmed) this.exitGame(game.id);
    });
  }

  exitGame(id:Number){
    this.gameService.exitGame(id).subscribe(
      () => {
        this.games.find(game => game.id == id).currentUserJoined = false;
        this.snackBar.open(this.exitGameLabel,'',{duration: 4000});
      }
    )
  }

}

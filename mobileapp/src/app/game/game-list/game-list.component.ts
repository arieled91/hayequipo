import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game, GameStatus} from "../game.model";
import {GameService} from "../service/game.service";
import {MatDialog, MatSnackBar} from "@angular/material";
import {GameDialogComponent, PlayersDialogComponent} from "../game.component";
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

  confirmExitGameLabel = "¿Estás seguro?";
  confirmNavigateToMapLabel = "¿Estás seguro que querés abrir el mapa?";
  cancelBtn = "Salir";
  playersBtn = "Anotados";
  openBtn = "Más";
  Status = GameStatus;
  itemsVisibility = [];
  locale = "es";

  constructor(private gameService: GameService, private dialog: MatDialog, private snackBar: MatSnackBar) {
  }

  ngOnInit() {}

  joinGame(id) {
    this.gameService.joinGame(id).subscribe(
      () => {
        this.games.find(game => game.id == id).currentUserJoined = true;
        this.snackBar.open('¡Anotado! Recordá que si te bajas traés facturas','',{duration: 4000});
      }
    )
  }

  exitGame(id){
    if(confirm(this.confirmExitGameLabel)) {
      this.gameService.exitGame(id).subscribe(
        () => {
          this.games.find(game => game.id == id).currentUserJoined = false;
          this.snackBar.open('¡Tenés que traer facturas!','',{duration: 4000});
        }
      )
    }
  }

  openGameFormDialog(id: Number){
    let dialogRef = this.dialog.open(GameDialogComponent, {
      minWidth: '50%',
      minHeight: '50%',
      data: {id : id}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.onDialogClose.emit(result.reload);
    });
  }

  openPlayersDialog(id: Number) {
    this.dialog.open(PlayersDialogComponent, {
      minWidth: '100px',
      minHeight: '100px',
      data: {id : id}
    });
  }

  openMap(game:Game){
    // if(confirm(this.confirmNavigateToMapLabel))
      window.location.assign(buildMapQueryByAddress(game.location.address)/*, "_blank"*/);
  }

  openMapConfirmDialog(game:Game){
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {content : this.confirmNavigateToMapLabel, openUrl: buildMapQueryByAddress(game.location.address)}
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if(confirmed) this.openMap(game);
    });
  }
}

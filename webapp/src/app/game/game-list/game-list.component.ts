import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game} from "../game.model";
import {GameService} from "../service/game.service";
import {MatDialog, MatSnackBar} from "@angular/material";
import {GameDialogComponent} from "../game-dialog.component";

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss']
})
export class GameListComponent implements OnInit {

  @Input() games : Game[];
  @Output() onDialogClose = new EventEmitter<boolean>();

  confirmExitGameLabel = "¿Estás seguro?";

  constructor(private gameService: GameService, private dialog: MatDialog, private snackBar: MatSnackBar) {
  }

  ngOnInit() {}

  joinGame(id) {
    this.gameService.joinGame(id).subscribe(
      data => {
        this.games.find(game => game.id == id).currentUserJoined = true;
        this.snackBar.open('¡Anotado! Recordá que si te bajas traés facturas','',{duration: 3000});
      }
    )
  }

  exitGame(id){
    if(confirm(this.confirmExitGameLabel)) {
      this.gameService.exitGame(id).subscribe(
        data => {
          this.games.find(game => game.id == id).currentUserJoined = false;
          this.snackBar.open('¡Esperamos las facturas!','',{duration: 4000});
        }
      )
    }
  }

  openGameFormDialog(id){
    let dialogRef = this.dialog.open(GameDialogComponent, {
      minWidth: '50%',
      minHeight: '50%',
      data: {id : id}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.onDialogClose.emit(result.reload);
    });
  }
}

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game} from "../game.model";
import {GameService} from "../service/game.service";
import {MatDialog} from "@angular/material";
import {GameDialogComponent} from "../game-dialog.component";

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss']
})
export class GameListComponent implements OnInit {

  @Input() games : Game[];
  @Output() onDialogClose = new EventEmitter<boolean>();

  confirmExitGameLabel = "¿Esta seguro que desea traer facturas?";

  constructor(private gameService: GameService, public dialog: MatDialog ) {
  }

  ngOnInit() {}

  joinGame(id) {
    this.gameService.joinGame(id).subscribe(
      data => {
        this.games.find(game => game.id == id).currentUserJoined = true;
        alert("asd233");
      }
    )
  }

  exitGame(id){
    if(confirm(this.confirmExitGameLabel)) {
      this.gameService.exitGame(id).subscribe(
        data => this.games.find(game => game.id == id).currentUserJoined = false
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

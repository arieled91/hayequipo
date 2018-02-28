import {Component, Inject, Input} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Game} from "./game.model";

@Component({
  selector: 'game-dialog',
  template: '<app-game-form [id]="id" (onSaved)="onSaved($event)"></app-game-form>',
})
export class GameDialogComponent {

  @Input() private id : Number;

  constructor(
    public dialogRef: MatDialogRef<GameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data
  ) {
    this.id = data.id;
  }

  onSaved(close: boolean){
    if(close) {
      this.dialogRef.close();
    }
  }

}

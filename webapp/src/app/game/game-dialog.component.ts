import {Component, Inject, Input} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from "@angular/material";
import {Game} from "./game.model";

@Component({
  selector: 'game-dialog',
  template: '<app-game-form [id]="id" (onSaved)="onSaved($event)" (onCancel)="onCancel()"></app-game-form>',
})
export class GameDialogComponent {

  @Input() private id : Number;

  constructor(
    public dialogRef: MatDialogRef<GameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data,
    public snackBar: MatSnackBar
  ) {
    this.id = data.id;
  }

  onSaved(close: boolean){
    if(close) {
      this.dialogRef.close({reload : true});
      this.snackBar.open('Guardado', '',{duration: 1500});
    }
  }
  onCancel(){
    this.dialogRef.close({reload : false});
    this.snackBar.open('Cancelado','',{duration: 1500});
  }

}

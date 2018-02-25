import {Component, EventEmitter, Inject, Output} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: 'game-dialog',
  template: '<app-game-form (onSaved)="onSaved($event)"></app-game-form>',
})
export class GameDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<GameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  onSaved(close: boolean){
    if(close) {
      this.dialogRef.close();
    }
  }

}

import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'confirm-dialog',
  template: `
    <h2 mat-dialog-title>{{title}}</h2>
    <mat-dialog-content>{{content}}</mat-dialog-content>
    <mat-dialog-actions>
      <button mat-button mat-dialog-close (click)="onConfirm.emit(false)">Cancelar</button>
      <button mat-button [mat-dialog-close]="true" (click)="onConfirm.emit(true)">Aceptar</button>
      <!--<a href="{{openUrl}}" target="_blank"><button mat-button [mat-dialog-close]="true">Abrir</button></a>-->
    </mat-dialog-actions>
  `
})
export class ConfirmDialogComponent implements OnInit {

  @Input() public title : string = "Confirmar";
  @Input() public content : string = "";
  @Input() public openUrl : String = "";

  @Output() onConfirm: EventEmitter<boolean> = new EventEmitter();

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.content = this.data.content;
    if(!isNullOrUndefined(this.data.title)) this.title = this.data.title;
    if(!isNullOrUndefined(this.data.openUrl)) this.openUrl = this.data.openUrl;
  }

  ngOnInit() {
  }

}

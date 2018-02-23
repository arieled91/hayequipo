import {AfterContentInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {GameService} from "./service/game.service";
import {MDCDialog} from '@material/dialog';

@Component({
  selector: 'app-game-search',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.scss']
})
export class GameSearchComponent implements OnInit, AfterContentInit{


  title = "Partidos";
  dateLabel = "Buscar";

  gameDialog;

  gameDate = null;
  games = [];

  @ViewChild('gameDialogRef') gameDialogRef: ElementRef;

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.find()
  }
  ngAfterContentInit(): void {
    this.gameDialog = new MDCDialog(this.gameDialogRef.nativeElement);
  }

  find() {
    this.gameService.findByDate(this.gameDate).subscribe(
      data => this.games = data
    );
    console.log(this.games);
  }


  openGameDialog() {
   this.gameDialog.show();
  }
}

import {Component, ElementRef, Input, NgZone, OnInit, ViewChild} from '@angular/core';
import {fieldTypes, Game} from "../game.model";
import {isNullOrUndefined} from "util";
import {GameService} from "../service/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material";
import {MapsAPILoader} from "@agm/core";
// noinspection ES6UnusedImports
import {} from '@types/googlemaps';

@Component({
  selector: 'app-game-form',
  templateUrl: './game-form.component.html',
  styleUrls: ['./game-form.component.scss']
})
export class GameFormComponent implements OnInit {

  @Input() id : number = null;
  title = "Partido";

  descriptionLabel = "Descripción";
  dateLabel = "Fecha";
  timeLabel = "Hora";
  locationDescLabel = "Lugar";
  locationAddrLabel = "Dirección";
  fieldTypeLabel = "Tipo de Cancha";
  game : Game = new Game();
  fields = fieldTypes;

  @ViewChild('searchAddress') public searchAddress: ElementRef;

  constructor(private gameService: GameService,
              private router: Router,
              private snackBar: MatSnackBar,
              private route: ActivatedRoute,
              private mapsAPILoader: MapsAPILoader, private ngZone: NgZone
  ) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = +params['id'];
    });

    if(!isNullOrUndefined(this.id) && this.id > 0) this.gameService.findById(this.id).subscribe(data => this.game = data);

    this.loadMapSearcher();
  }

  loadMapSearcher() {
    this.mapsAPILoader.load().then(
      () => {
        let autocomplete = new google.maps.places.Autocomplete(this.searchAddress.nativeElement, { types:["address"] });

        autocomplete.addListener("place_changed", () => {
          this.ngZone.run(() => {
            let place: google.maps.places.PlaceResult = autocomplete.getPlace();

            this.game.location.latitude = place.geometry.location.lat();
            this.game.location.longitude = place.geometry.location.lng();
            this.game.location.address = place.formatted_address;

            if(place.geometry === undefined || place.geometry === null ){
              return;
            }
          });
        });
      }
    );
  }


  save() {
    this.gameService.saveGame(this.game).subscribe(
    data => {
      this.router.navigate(["game-list"]);
      this.router.navigate(["game"]);
    },
    error => this.snackBar.open(error)
    );
  }

  // setTime(event){
  //   let time = event;
  //   this.game.dateTime = moment(this.game.dateTime).hours(isNullOrUndefined(time) ? 0 : event.substring(0,2));
  //   this.game.dateTime = moment(this.game.dateTime).minutes(isNullOrUndefined(time) ? 0 : event.substring(3,5));
  //   this.game.dateTime = moment(this.game.dateTime).seconds(0);
  //   this.game.dateTime = moment(this.game.dateTime).milliseconds(0);
  //
  //   console.log(this.game.dateTime);
  // }
  //
  // getGameTime(){
  //   return isNullOrUndefined(this.game.dateTime) ? this.game.dateTime : moment(this.game.dateTime).format("HH:mm")
  // }

  getCapacityValue() {
    return this.fields.find(g => g.capacity==this.game.capacity)
  }
}

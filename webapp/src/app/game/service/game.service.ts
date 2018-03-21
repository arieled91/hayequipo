import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Game, Player} from "../game.model";
import {DatePipe} from "@angular/common";
import {Observable} from "rxjs/Observable";
import {isNullOrUndefined} from "util";
import Api from "../../service/api.util";
import * as moment from 'moment';

@Injectable()
export class GameService {

  private gamesUrl = Api.URL+"/controller/games";
  private findUrl = "/controller/games/find";

  constructor(private http: HttpClient) {}

  findByDate(date: Date) : Observable<any>{
    const pipe = new DatePipe('en-US');
    const dateParam = isNullOrUndefined(date) ? "": "date="+pipe.transform(date,"yyyy-MM-dd");
    let requestUrl = Api.request(this.findUrl, dateParam);
    return this.http.get<Game>(requestUrl);
  }

  saveGame(game: Game) : Observable<any>{
    game.dateTime = moment(game.dateTime).utc(true); //fixes utc time
    if(isNullOrUndefined(game)) throw new Error("save - Game cannot be: "+game);
    return this.http.post<Game>(this.gamesUrl+"/save", game);
  }

  joinGame(gameId: Number) : Observable<any>{
    return this.http.get<Game>(`${this.gamesUrl}/${gameId}/join`);
  }

  exitGame(gameId: Number) : Observable<any>{
    return this.http.get<Game>(`${this.gamesUrl}/${gameId}/remove`);
  }

  findById(gameId: Number) : Observable<any>{
    return this.http.get<Game>(`${this.gamesUrl}/${gameId}`);
  }

  listPlayers(gameId: Number) : Observable<Array<Player>>{
    return this.http.get<Array<Player>>(`${this.gamesUrl}/${gameId}/players`);
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Api} from "../../service/api.util";
import {Game} from "../game.model";
import {DatePipe} from "@angular/common";
import {Observable} from "rxjs/Observable";
import {isUndefined, isNullOrUndefined} from "util";

@Injectable()
export class GameService {

  constructor(private http: HttpClient) {}

  findByDate(date: Date) : Observable<any>{
    const pipe = new DatePipe('en-US');
    const dateParam = isNullOrUndefined(date) ? "": "date="+pipe.transform(date,"yyyy-MM-dd");
    let requestUrl = Api.request("/games/find", dateParam);
    return this.http.get<Game>(requestUrl);
  }

  addNewGame(game: Game) : Observable<any>{
    if(isNullOrUndefined(game)) throw new Error("addNewGame - Game cannot be: "+game);
    let requestUrl = Api.request("/games", "");
    return this.http.post<Game>(requestUrl, game);
  }
}

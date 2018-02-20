import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Api} from "../../service/api.util";
import {Game} from "../game.interfaces";
import {DatePipe} from "@angular/common";
import {Observable} from "rxjs/Observable";
import {isUndefined} from "util";

@Injectable()
export class GameService {

  constructor(private http: HttpClient) {}

  findByDate(date: Date) : Observable<any>{
    const pipe = new DatePipe('en-US');
    const dateParam = (date===null || date.toString()==="" ? "": "date="+pipe.transform(date,"yyyy-MM-dd"));
    let requestUrl = Api.request("games/find", dateParam);
    return this.http.get<Game>(requestUrl);
  }
}

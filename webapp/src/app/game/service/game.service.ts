import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Api} from "../../service/api.util";
import {Game} from "../interfaces";
import {DatePipe} from "@angular/common";
import {Observable} from "rxjs/Observable";

@Injectable()
export class GameService {

  constructor(private http: HttpClient) {}

  findByDate(date: Date) : Observable<any>{
    const pipe = new DatePipe('en-US');
    let requestUrl = Api.request("games/find", "date="+pipe.transform(date,"yyyy-MM-dd"));
    return this.http.get<Game>(requestUrl);
  }
}

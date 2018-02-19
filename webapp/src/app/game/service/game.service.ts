import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Api} from "../../service/api.util";
import {Game} from "../interfaces";

@Injectable()
export class GameService {

  constructor(private http: HttpClient) {}

  findByDate(date: Date){
    let requestUrl = Api.request("games/find", "date="+date);
    this.http.get<Game>(requestUrl).subscribe(data => console.log(data));
  }
}

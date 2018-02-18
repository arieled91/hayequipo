import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Api} from "../../service/api";
import {Router} from "@angular/router";

@Injectable()
export class GameService {

  constructor(private router: Router, private http: HttpClient) {}

  findByDate(date: Date){
    let request = Api.request("games/find", "date="+date);
    this.http.get(request).subscribe(
      data => console.log(data),
      error => {
        this.router.navigate([Api.url()+'login']);
        console.error('An error occurred in dashboard component, navigating to login: ', error);
      }
    )
  }

}

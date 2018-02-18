import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Api} from "../../service/api";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../service/authentication.service";
import {Game} from "../interfaces";

@Injectable()
export class GameService {

  constructor(
    private router: Router,
    private http: HttpClient,
    private authenticationService: AuthenticationService
  ) {}

  /*
  addHero (hero: Hero): Observable<Hero> {
  return this.http.post<Hero>(this.heroesUrl, hero, httpOptions)
    .pipe(
      catchError(this.handleError('addHero', hero))
    );
}

   */
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      'Authorization': 'Bearer ' + AuthenticationService.getToken()
    })
  };

  findByDate(date: Date){

    let requestUrl = Api.request("games/find", "date="+date);

    console.log(this.httpOptions);

    this.http.get<Game>(requestUrl, this.httpOptions)
      .subscribe(
        data => console.log(data),
        error => this.router.navigate(['login'])
      );

  }

}

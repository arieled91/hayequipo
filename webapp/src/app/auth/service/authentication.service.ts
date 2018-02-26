import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {HttpClient} from "@angular/common/http";
import {TokenResponse, User} from "../auth.interfaces";
import Api from "../../service/api.util";

@Injectable()
export class AuthenticationService {
  private pingUrl = Api.BASE_URL+'/api/ping';
  private authUrl = Api.BASE_URL+'/auth/login';
  private userUrl = Api.BASE_URL+'/auth/user';
  private options = {headers: {'Content-Type': 'application/json'}};


  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post(this.authUrl, JSON.stringify({username: username, password: password}), this.options)
      .map((response: TokenResponse) => {
        console.log(response);
        // login successful if there's a jwt token in the response
        let token = response.token;
        if (token) {
          // store username and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify({ username: username, token: token }));

          // return true to indicate successful login
          return true;
        } else {
          // return false to indicate failed login
          return false;
        }
      }).catch((error:any) => Observable.throw(error.error || 'Server error'));
  }

  static getUser(): any{
    return JSON.parse(localStorage.getItem('currentUser'));
  }

  static getToken(): String {
    const currentUser = AuthenticationService.getUser();
    const token = currentUser && currentUser.token;
    return token ? token : "";
  }

  static logout(): void {
    // clear token remove user from local storage to log user out
    localStorage.removeItem('currentUser');
  }

  findCurrentUser(): Observable<User>{
    return this.http.get<User>(this.userUrl)
  }

  ping(){
    return this.http.get<User>(this.pingUrl).subscribe()
  }
}

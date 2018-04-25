import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {HttpClient} from "@angular/common/http";
import {User} from "../auth.model";
import Api from "../../service/api.util";

@Injectable()
export class AuthService {
  private pingUrl = Api.BASE_URL+'/api/ping';
  private userUrl = Api.BASE_URL+'/auth/user';


  public static SESSION_ID_KEY = "sessionid";


  constructor(private http: HttpClient) {}


  static getUser(): any{
    return JSON.parse(localStorage.getItem('currentUser'));
  }

  removeToken() {
    localStorage.removeItem(AuthService.SESSION_ID_KEY)
  }

  saveToken(token: string) {
    this.removeToken();
    localStorage.setItem(AuthService.SESSION_ID_KEY, token)
  }

  getToken(): String {
    return localStorage.getItem(AuthService.SESSION_ID_KEY)
  }

  logout(): void {
    // clear token remove user from local storage to log user out
    localStorage.removeItem('currentUser');
  }

  findCurrentUser(): Observable<User>{
    return this.http.get<User>(this.userUrl)
  }

  ping(){
    return this.http.get<User>(this.pingUrl).subscribe(
      // data => console.log(data)
    )
  }
}

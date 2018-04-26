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
  private pingUrl = '/api/ping';
  private userUrl = '/auth/user';


  public static SESSION_ID_KEY = "sessionid";


  constructor(private http: HttpClient) {}


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

  findCurrentUser(): Observable<User>{
    return this.http.get<User>(this.userUrl)
  }

  ping(){
    return this.http.get<User>(this.pingUrl).subscribe(
      // data => console.log(data)
    )
  }
}

import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';



import {HttpClient} from "@angular/common/http";
import {User} from "../auth.model";
import {PagedList} from "../../common/common.model";

@Injectable()
export class AuthService {
  private pingUrl = '/api/ping';
  private userUrl = '/auth/users';
  private userRepositoryUrl = '/api/users';


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
    return this.http.get<User>(this.userUrl+'/current')
  }

  listUsers(): Observable<PagedList<User>>{
    return this.http.get<any>(this.userRepositoryUrl).map(data => {
      let pagedList = new PagedList<User>();
      pagedList.data = data._embedded.users;
      pagedList.page = data.page;
      return pagedList;
    })
  }

  ping(){
    return this.http.get<User>(this.pingUrl).subscribe(
      // data => console.log(data)
    )
  }
}

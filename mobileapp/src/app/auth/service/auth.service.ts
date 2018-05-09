import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';


import {HttpClient,HttpParams} from '@angular/common/http';
import {User} from "../auth.model";
import {PagedList} from "../../common/common.model";
import Api from "../../service/api.util";

@Injectable()
export class AuthService {
  private pingUrl = '/api/ping';
  private userUrl = '/auth/users';
  private userListSearchUrl = '/api/users/search/findAllByEnabledAndQuery?enabled=true';


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

  listUsers(query: string, sort: string, order: string, page: number, size: number): Observable<PagedList<User>>{

    const params : HttpParams = new HttpParams().append("query",query);
    Api.addPageParams(params, page, sort, order, size);

    return this.http.get<any>(this.userListSearchUrl, {params: params})
      .map(data => {
        let pagedList = new PagedList<User>();
        pagedList.data = data._embedded.users;
        pagedList.page = data.page;
        return pagedList;
      })
  }

  getPrivileges(): Observable<Set<String>>{
    return this.http.get<Set<String>>(this.userUrl+'/current/privileges')
  }
}

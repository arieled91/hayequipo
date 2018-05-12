import {Injectable} from '@angular/core';


import {HttpClient, HttpParams} from '@angular/common/http';
import {TokenResponse, User, UserRegistration} from "../auth.model";
import {PagedList} from "../../common/common.model";
import Api from "../../service/api.util";
import {isNullOrUndefined} from "util";
import {Observable, throwError} from "rxjs";
import {map, catchError} from "rxjs/operators"

@Injectable()
export class AuthService {
  private pingUrl = '/api/ping';
  private userUrl = '/auth/users';
  private authUrl = '/auth/login';
  private userListSearchUrl = '/api/users/search/findAllByEnabledAndQuery?enabled=true';
  private registrationUrl = '/auth/registration';

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
      .pipe(map(data => {
        let pagedList = new PagedList<User>();
        pagedList.data = data._embedded.users;
        pagedList.page = data.page;
        return pagedList;
      }))

  }

  getPrivileges(): Observable<Set<String>>{
    return this.http.get<Set<String>>(this.userUrl+'/current/privileges')
  }

  register(user: UserRegistration) : Observable<any>{
    if(isNullOrUndefined(user)) throw new Error("Register - UserRegistration cannot be: "+user);
    return this.http.post<UserRegistration>(this.registrationUrl, user);
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post(this.authUrl, {username: username, password: password}).pipe(
      map((response: TokenResponse) => {
        // login successful if there's a jwt token in the response
        let token = response.token;
        if (token!==null && token.length>0) {
          // return true to indicate successful login
          this.saveToken(token);
          console.log("token ok");
          return true;
        } else {
          // return false to indicate failed login
          return false;
        }
      }),
      catchError((error:any) => throwError(error.error || 'Server error'))
    )
  }
}

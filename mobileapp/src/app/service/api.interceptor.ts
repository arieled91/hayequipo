import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';


import {Router} from '@angular/router';
import {AuthService} from "../auth/service/auth.service";
import Api from "./api.util";

@Injectable()
export class ApiHttpInterceptor implements HttpInterceptor {

  TOKEN_KEY = 'Authorization';

  errorMessage = "Error. Por favor intente más tarde.";

  constructor(private authService : AuthService, private router: Router) { }


  private baseUrl: string = Api.BASE_URL;

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

      const headers = req.headers
          .append(this.TOKEN_KEY, 'Bearer ' + this.authService.getToken() + '')
          .append('Content-Type','application/json;charset=UTF-8');

    const apiReq = req.clone({ headers: headers, url: `${this.baseUrl}${req.url}` });
    return next.handle(apiReq)
        .catch((error, caught) => {

          //intercept the respons error and displace it to the console
          console.log("Error Occurred");
          console.log(error);

          //if unauthorized navigate to login
          console.log(error.status);
          if(error.status===401) this.router.navigate(['login']);

          if(error.status===404) alert(this.errorMessage);
          console.log(error);
          //return the error to the method that called it
          return Observable.throw(error);
        }) as any;
  }
}

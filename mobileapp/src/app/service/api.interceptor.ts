import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/observable/throw'
import 'rxjs/add/operator/catch';
import {Router} from '@angular/router';
import {AuthService} from "../auth/service/auth.service";
import Api from "./api.util";

@Injectable()
export class ApiHttpInterceptor implements HttpInterceptor {

  TOKEN_KEY = 'X-Auth-Token';

  // skipSecurityRoutes = ["/register","/login"];
  //
  // errorMessage = "Error. Por favor intente más tarde.";
  //
  constructor(private authService : AuthService) { }
  //
  // intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  //
  //   // Clone the request to add the new header.
  //   const authReq = req.clone({ headers: req.headers
  //       .append("Access-Control-Allow-Origin", "*")
  //       .append("Authorization", "" + this.authService.getToken() + "")
  //       .append("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS")
  //       .append("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization")
  //       .append("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization")
  //       .append("Access-Control-Allow-Credentials", "true")
  //       .append("Access-Control-Max-Age", "3600")
  //   });
  //
  //   console.log("Sending request... ("+req.url+")");
  //
  //   //send the newly created request
  //   return next.handle(authReq)
  //     .catch((error, caught) => {
  //
  //       //intercept the respons error and displace it to the console
  //       console.log("Error Occurred");
  //       console.log(error);
  //
  //       //if unauthorized navigate to login
  //       console.log(error.status);
  //       if(error.status===401 && this.isSecureUrl()) this.router.navigate(['login']);
  //
  //       if(error.status===404) alert(this.errorMessage);
  //       console.log(error);
  //       //return the error to the method that called it
  //       return Observable.throw(error);
  //     }) as any;
  // }
  //
  // isSecureUrl(){
  //   return !this.skipSecurityRoutes.includes(this.router.url);
  // }

  private baseUrl: string = Api.BASE_URL;

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

      const headers = req.headers
          // .append("Access-Control-Allow-Origin", "*")
          .append(this.TOKEN_KEY, '' + this.authService.getToken() + '')
          .append('Content-Type','application/json;charset=UTF-8');
          // .append("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS")
          // .append("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,"+this.TOKEN_KEY)
          // .append("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,"+this.TOKEN_KEY)
          // .append("Access-Control-Allow-Credentials", "true")
          // .append("Access-Control-Max-Age", "3600");


    const apiReq = req.clone({ headers: headers, url: `${this.baseUrl}${req.url}` });
    return next.handle(apiReq);
  }
}

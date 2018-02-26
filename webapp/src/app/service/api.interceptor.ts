import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/observable/throw'
import 'rxjs/add/operator/catch';
import {AuthenticationService} from "../auth/service/authentication.service";
import {Router} from '@angular/router';

@Injectable()
export class ApiHttpInterceptor implements HttpInterceptor {
  constructor(private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // Clone the request to add the new header.
    const authReq = req.clone({ headers: req.headers
        .append("Access-Control-Allow-Origin", "*")
        .append("Authorization", "" + AuthenticationService.getToken() + "")
        .append("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS")
        .append("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization")
        .append("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization")
        .append("Access-Control-Allow-Credentials", "true")
        .append("Access-Control-Max-Age", "3600")
    });

    console.log("Sending request... ("+req.url+")");

    //send the newly created request
    return next.handle(authReq)
      .catch((error, caught) => {

        //intercept the respons error and displace it to the console
        console.log("Error Occurred");
        console.log(error);

        //if unauthorized navigate to login
        console.log(error.status);
        if(error.status===401) this.router.navigate(['login']);

        //return the error to the method that called it
        return Observable.throw(error);
      }) as any;
  }
}

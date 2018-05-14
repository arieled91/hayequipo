import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {AuthService} from "../service/auth.service";
import {MatSnackBar} from "@angular/material";
import Api from "../../service/api.util";
import {isNullOrUndefined} from "util";


@Component({
  selector: 'app-login',
  moduleId: module.id,
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.scss']
})

export class LoginComponent implements OnInit {

  title = "Identificación";

  loading = false;
  error = '';
  message = "";
  token = null;
  model: any = {};
  googleLoginLbl = "Ingresá con Google";
  usernameLabel = "Usuario";
  passwordLabel = "Contraseña";
  loginBtn = "Ingresá";
  registerBtn = "Registrate";
  passwordRequiredLabel = "Se requiere una contraseña";
  usernameRequiredLabel = "Se requiere un usuario";
  userPassIncorrectError = "Usuario o contraseña inválidos";
  validateMailMessage = "¡Listo! Confirmá tu correo para poder ingresar.";

  public googleAuthUrl = Api.BASE_URL+'/oauth2/authorization/google';

  constructor(
    private router: Router,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
  ) {
    this.route.queryParams.subscribe(params => this.populate(params));
  }

  populate(params: Params){
    if(params['token']) this.token = params['token'];
    if(params['message']) this.snackBar.open( params['message'],'',{duration: 10000});
    if(params['registered']) this.snackBar.open(this.validateMailMessage,'',{duration: 10000});
  }

  ngOnInit() {
    this.logout();
    if(!isNullOrUndefined(this.token)) this.login();
  }


  googleLogin(){
    window.location.href = this.googleAuthUrl;
  }


  login() {
    this.authService.saveToken(this.token);
    this.router.navigate(["home"]);
  }

  logout() {
    localStorage.clear();
  }

  ownLogin() {
    this.loading = true;
    this.authService.login(this.model.username, this.model.password)
      .subscribe(result => {
        if (result === true) {
          // login successful
          this.snackBar.dismiss();
          this.router.navigate(['home']);
        } else {
          // login failed
          this.loading = false;
          this.snackBar.open(this.userPassIncorrectError);
        }
      }, error => {
        this.loading = false;
        this.snackBar.open(this.userPassIncorrectError);
      });
  }

}

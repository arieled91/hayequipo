import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {AuthenticationService} from "../service/authentication.service";
import {MatSnackBar} from "@angular/material";


@Component({
  selector: 'app-login',
  moduleId: module.id,
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.scss']
})

export class LoginComponent implements OnInit {
  model: any = {};
  loading = false;
  error = '';

  title = "";
  usernameLabel = "Usuario";
  passwordLabel = "Contraseña";
  loginBtn = "Ingresá";
  registerBtn = "Registrate";
  passwordRequiredLabel = "Se requiere una contraseña";
  usernameRequiredLabel = "Se requiere un usuario";
  userPassIncorrectError = "Usuario o contraseña inválidos";
  validateMailMessage = "¡Listo! Confirmá tu correo para poder ingresar.";
  message = "";

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
  ) {
    this.route.queryParams.subscribe(params => this.populate(params));
  }

  ngOnInit() {
    // reset login status
    AuthenticationService.logout();

    if(this.message.length>0) this.snackBar.open(this.message);
  }

  populate(params: Params){
    console.log(params);
    if(params['registered']) this.message = this.validateMailMessage;
  }


  login() {
    this.loading = true;
    this.authenticationService.login(this.model.username, this.model.password)
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

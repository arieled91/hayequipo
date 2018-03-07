import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  loginBtn = "INGRESÁ";
  passwordRequiredLabel = "Se requiere una contraseña";
  usernameRequiredLabel = "Se requiere un usuario";
  userPassIncorrectError = "Usuario o contraseña inválidos";

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar) { }

  ngOnInit() {
    // reset login status
    AuthenticationService.logout();
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

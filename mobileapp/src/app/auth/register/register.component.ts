import {Component, OnInit} from '@angular/core';
import {UserRegistration} from "../auth.model";
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  loading = false;
  title = "Registrarse";
  emailLabel = "Email";
  passwordLabel = "Contraseña";
  firstNameLabel = "Nombre";
  lastNameLabel = "Apellido";
  confirmPasswordLabel = "Confirmar Contraseña";
  registerBtn = "Enviar";
  cancelBtn = "Cancelar";
  requiredLabel = "Campo requerido";
  passwordsDontMatchLabel = "las contraseñas no coinciden";

  user : UserRegistration = new UserRegistration();
  confirmPasswordModel : String = null;
  error = "";
  info = "";

  constructor(private authService : AuthService, private router: Router) { }

  ngOnInit() {
  }

  register(){
    if(this.user.password!==this.confirmPasswordModel) {
      this.error = this.passwordsDontMatchLabel;
      return;
    }else this.error = "";

    this.loading = true;
    this.authService.register(this.user).subscribe(
      data => this.router.navigate(['login'],{queryParams : {registered : true}}),
        error => this.error = error.error.message
    );
    this.loading = false;
  }

}

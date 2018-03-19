import {Component, OnInit} from '@angular/core';
import {UserRegistration} from "../auth.model";
import {AuthenticationService} from "../service/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

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

  constructor(private authService : AuthenticationService, private router: Router) { }

  ngOnInit() {
  }

  register(){
    if(this.user.password!==this.confirmPasswordModel) {
      this.error = this.passwordsDontMatchLabel;
      return;
    }else this.error = "";

    this.authService.register(this.user).subscribe(
      data => this.router.navigate(['login']),
        error => this.error = error.error.message
    );
  }

}

import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  standalone: false,
  selector: 'app-admin-login',
  templateUrl: './admin-login.html',
  styleUrls: ['./admin-login.css']
})
export class AdminLogin {

  formulario = { usuario: '', contrasenia: '', correo: '' };
  cargando = false;
  mensajeError = '';

  constructor(private authService: AuthService, private router: Router) {}

  enviarFormulario(form: NgForm): void {
    if (form.invalid) return;
    this.cargando = true;
    this.mensajeError = '';

    this.authService.iniciarSesion(this.formulario.usuario, this.formulario.contrasenia, this.formulario.correo).subscribe({
      next: (token) => {
        this.authService.guardarToken(token);
        this.cargando = false;
        this.router.navigate(['/admin']);
      },
      error: () => {
        this.cargando = false;
        this.mensajeError = 'Credenciales incorrectas.';
      }
    });
  }
}

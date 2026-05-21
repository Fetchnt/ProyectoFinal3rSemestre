import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  standalone: false,
  selector: 'app-log-in',
  templateUrl: './log-in.html',
  styleUrls: ['./log-in.css']
})
export class LogIn {

  formulario = { usuario: '', contrasenia: '' };
  mostrarContrasenia = false;
  cargando = false;
  mensajeError = '';
  mensajeExito = '';

  constructor(private router: Router, private authService: AuthService) {}

  enviarFormulario(form: NgForm): void {
    if (form.invalid) {
      this.mensajeError = 'Completa todos los campos.';
      return;
    }
    this.cargando = true;
    this.mensajeError = '';

    this.authService.iniciarSesion(this.formulario.usuario, this.formulario.contrasenia).subscribe({
      next: (token) => {
        this.authService.guardarToken(token);
        const id = this.authService.obtenerIdDesdeToken();
        this.authService.guardarClienteId(id);
        this.authService.guardarToken(token);
        this.mensajeExito = '¡Acceso concedido!';
        this.cargando = false;
        setTimeout(() => this.router.navigate(['/traductor']), 800);
      },
      error: () => {
        this.mensajeError = 'Usuario o contraseña incorrectos.';
        this.cargando = false;
      }
    });
  }

  alternarContrasenia(): void {
    this.mostrarContrasenia = !this.mostrarContrasenia;
  }
}

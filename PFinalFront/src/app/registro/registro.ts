import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  standalone: false,
  selector: 'app-registro',
  templateUrl: './registro.html',
  styleUrls: ['./registro.css']
})
export class Registro {

  formulario = { usuario: '', contrasenia: '', correo: '', codigoVerificacion: '' };
  paso = 1;
  cargando = false;
  mensajeError = '';
  mensajeExito = '';

  constructor(private authService: AuthService, private router: Router) {}

  registrar(form: NgForm): void {
    if (form.invalid) return;
    this.cargando = true;
    this.mensajeError = '';

    this.authService.registrarUsuario(this.formulario).subscribe({
      next: () => {
        this.cargando = false;
        this.mensajeExito = 'Revisa tu correo para verificar tu cuenta.';
        this.paso = 2;
      },
      error: () => {
        this.cargando = false;
        this.mensajeError = 'Error al registrar. El usuario o correo ya existe.';
      }
    });
  }

  verificar(): void {
    this.cargando = true;
    this.mensajeError = '';

    this.authService.verificarCuenta(this.formulario.correo, this.formulario.codigoVerificacion).subscribe({
      next: () => {
        this.cargando = false;
        this.mensajeExito = '¡Cuenta verificada! Redirigiendo...';
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: () => {
        this.cargando = false;
        this.mensajeError = 'Código inválido. Intenta de nuevo.';
      }
    });
  }
}

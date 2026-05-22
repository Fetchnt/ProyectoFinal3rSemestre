import { Component, ChangeDetectorRef } from '@angular/core';
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

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  registrar(form: NgForm): void {
    if (form.invalid) return;

    if (this.formulario.usuario.length < 5 || !/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/.test(this.formulario.usuario)) {
      this.mensajeError = 'El usuario debe tener mínimo 5 letras, sin números ni símbolos.';
      return;
    }
    if (this.formulario.contrasenia.length < 12) {
      this.mensajeError = 'La contraseña debe tener mínimo 12 caracteres.';
      return;
    }
    const dominiosValidos = ['gmail.com', 'hotmail.com', 'outlook.com', 'yahoo.com', 'unbosque.edu.co'];
    const dominio = this.formulario.correo.split('@')[1];
    if (!dominio || !dominiosValidos.includes(dominio)) {
      this.mensajeError = 'El correo debe ser Gmail, Hotmail, Outlook, Yahoo o unbosque.edu.co';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    this.authService.registrarUsuario(this.formulario).subscribe({
      next: () => {
        this.cargando = false;
        this.mensajeExito = 'Revisa tu correo para verificar tu cuenta.';
        this.paso = 2;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.cargando = false;
        this.mensajeError = err.status === 409
          ? 'El usuario o correo ya está registrado.'
          : 'Error al registrar. Intenta de nuevo.';
        this.cdr.detectChanges();
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
        this.cdr.detectChanges();
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: () => {
        this.cargando = false;
        this.mensajeError = 'Código inválido. Intenta de nuevo.';
        this.cdr.detectChanges();
      }
    });
  }
}

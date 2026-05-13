// src/app/log-in/log-in.ts
import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  standalone: false,
  selector: 'app-log-in',
  templateUrl: './log-in.html',
  styleUrls: ['./log-in.css']
})
export class LogIn {

  formData = {
    nombre: '',
    apellido: '',
    username: '',
    correo: '',
    password: ''
  };

  showPassword = false;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private router: Router) {}

  onSubmit(form: NgForm): void {
    if (form.invalid) {
      this.errorMessage = 'Por favor completa todos los campos requeridos.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    setTimeout(() => {
      this.isLoading = false;
      this.successMessage = '¡Acceso concedido!';
      setTimeout(() => this.router.navigate(['/traductor']), 800);
    }, 1000);
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }
}

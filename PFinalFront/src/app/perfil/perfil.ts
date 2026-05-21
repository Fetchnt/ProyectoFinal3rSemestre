import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { CodigoService } from '../services/codigo.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-perfil',
  standalone: false,
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class Perfil implements OnInit {

  nombreUsuario = '';
  totalTraducciones = 0;
  historial: any[] = [];

  constructor(
    private location: Location,
    private codigoService: CodigoService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.nombreUsuario = localStorage.getItem('usuarioNombre') || 'Usuario';
    this.codigoService.count().subscribe({
      next: (total) => this.totalTraducciones = total,
      error: () => this.totalTraducciones = 0
    });
  }

  volver(): void {
    this.location.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CodigoService } from '../services/codigo.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin implements OnInit {

  totalTraducciones = 0;
  listaUsuarios: any[] = [];
  listaTraducciones: any[] = [];

  constructor(
    private codigoService: CodigoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.codigoService.count().subscribe({
      next: (total) => this.totalTraducciones = total,
      error: () => {}
    });

    this.codigoService.getAll().subscribe({
      next: (datos) => {
        try { this.listaTraducciones = JSON.parse(datos); } catch { this.listaTraducciones = []; }
      },
      error: () => {}
    });
  }

  cerrarSesionAdmin(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/admin-login']);
  }
}

import { Component, OnInit } from '@angular/core';
import { CodigoService } from '../services/codigo.service';
import { AuthService } from '../services/auth.service';

@Component({
  standalone: false,
  selector: 'app-traductor',
  templateUrl: './traductor.html',
  styleUrls: ['./traductor.css']
})
export class Traductor implements OnInit {

  proveedores: string[] = [];
  lenguajes: string[] = [];

  proveedorSeleccionado = '';
  lenguajeOrigen = '';
  lenguajeDestino = '';
  codigoEntrada = '';
  codigoSalida = '';
  traduciendo = false;
  copiado = false;
  mensajeError = '';

  constructor(private codigoService: CodigoService, private authService: AuthService) {}

  ngOnInit(): void {
    this.codigoService.getProveedores().subscribe({
      next: (lista) => this.proveedores = lista,
      error: () => this.proveedores = []
    });
    this.codigoService.getLenguajes().subscribe({
      next: (lista) => this.lenguajes = lista,
      error: () => this.lenguajes = []
    });
  }

  puedeTraducir(): boolean {
    return !!this.proveedorSeleccionado && !!this.lenguajeOrigen &&
      !!this.lenguajeDestino && !!this.codigoEntrada.trim() &&
      this.lenguajeOrigen !== this.lenguajeDestino;
  }

  traducir(): void {
    if (!this.puedeTraducir()) return;
    this.traduciendo = true;
    this.mensajeError = '';
    this.codigoSalida = '';

    const peticion = {
      lenguajeRecibido: this.lenguajeOrigen,
      lenguajeATraducir: this.lenguajeDestino,
      proveedorIA: this.proveedorSeleccionado,
      codigoRecibido: this.codigoEntrada,
      clienteId: this.authService.obtenerClienteId()
    };

    this.codigoService.traducirConProveedor(peticion as any).subscribe({
      next: (resultado) => {
        this.codigoSalida = resultado.codigoTraducido || '';
        this.traduciendo = false;
      },
      error: () => {
        this.mensajeError = 'Error al traducir. Verifica que el back esté corriendo.';
        this.traduciendo = false;
      }
    });
  }

  copiarSalida(): void {
    if (!this.codigoSalida) return;
    navigator.clipboard.writeText(this.codigoSalida).then(() => {
      this.copiado = true;
      setTimeout(() => this.copiado = false, 2000);
    });
  }

  limpiar(): void {
    this.codigoEntrada = '';
    this.codigoSalida = '';
    this.mensajeError = '';
  }

  intercambiarLenguajes(): void {
    const temp = this.lenguajeOrigen;
    this.lenguajeOrigen = this.lenguajeDestino;
    this.lenguajeDestino = temp;
  }
}

import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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

  constructor(
    private codigoService: CodigoService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.codigoService.getProveedores().subscribe({
      next: (lista) => { this.proveedores = lista; this.cdr.detectChanges(); },
      error: () => this.proveedores = []
    });
    this.codigoService.getLenguajes().subscribe({
      next: (lista) => { this.lenguajes = lista; this.cdr.detectChanges(); },
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
        if (resultado.inteligenciasUsadas && resultado.inteligenciasUsadas.length > 0) {
          const primera = resultado.inteligenciasUsadas[0];
          if (primera.codigoRecibido && primera.codigoRecibido.startsWith('ERROR')) {
            this.mensajeError = 'Error de la IA: ' + primera.codigoRecibido;
            this.traduciendo = false;
            this.cdr.detectChanges();
            return;
          }
          this.codigoSalida = primera.codigoRecibido || '';
        } else {
          this.codigoSalida = resultado.codigoTraducido || '';
        }
        this.traduciendo = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.mensajeError = 'Error al traducir.';
        this.traduciendo = false;
        this.cdr.detectChanges();
      }
    });
  }

  copiarSalida(): void {
    if (!this.codigoSalida) return;
    navigator.clipboard.writeText(this.codigoSalida).then(() => {
      this.copiado = true;
      this.cdr.detectChanges();
      setTimeout(() => { this.copiado = false; this.cdr.detectChanges(); }, 2000);
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

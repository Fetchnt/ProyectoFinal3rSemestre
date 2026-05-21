import { Component } from '@angular/core';
import { CodigoService } from '../services/codigo.service';
import { AuthService } from '../services/auth.service';

interface LineaConsola {
  tipo: 'info' | 'salida' | 'error' | 'sistema';
  texto: string;
}

@Component({
  standalone: false,
  selector: 'app-runcode',
  templateUrl: './runcode.html',
  styleUrls: ['./runcode.css']
})
export class Runcode {

  lenguajeSeleccionado = 'python';
  codigoEntrada = '';
  lineasConsola: LineaConsola[] = [];
  ejecutando = false;

  constructor(private codigoService: CodigoService, private authService: AuthService) {}

  ejecutarCodigo(): void {
    if (!this.codigoEntrada.trim() || this.ejecutando) return;
    this.ejecutando = true;

    this.lineasConsola.push({ tipo: 'sistema', texto: `▶ Ejecutando en ${this.lenguajeSeleccionado}...` });

    const peticion = {
      codigoTraducido: this.codigoEntrada,
      lenguajeATraducir: this.lenguajeSeleccionado,
      codigoRecibido: this.codigoEntrada,
      lenguajeRecibido: this.lenguajeSeleccionado,
      proveedorIA: '',
      clienteId: this.authService.obtenerClienteId()
    };

    this.codigoService.ejecutarCodigo(peticion as any).subscribe({
      next: (resultado) => {
        this.lineasConsola.push({ tipo: 'salida', texto: resultado });
        this.ejecutando = false;
        this.desplazarAbajo();
      },
      error: () => {
        this.lineasConsola.push({ tipo: 'error', texto: 'Error al ejecutar el código.' });
        this.ejecutando = false;
      }
    });
  }

  limpiarConsola(): void {
    this.lineasConsola = [];
  }

  private desplazarAbajo(): void {
    setTimeout(() => {
      const consola = document.querySelector('.terminal-output');
      if (consola) consola.scrollTop = consola.scrollHeight;
    }, 50);
  }

  obtenerClaseLinea(tipo: string): string {
    const clases: Record<string, string> = {
      sistema: 'line-system',
      salida: 'line-output',
      error: 'line-error',
      info: 'line-info'
    };
    return clases[tipo] || '';
  }
}

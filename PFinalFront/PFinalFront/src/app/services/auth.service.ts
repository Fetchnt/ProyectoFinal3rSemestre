import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private urlBase = 'http://localhost:8080/usuario';

  constructor(private http: HttpClient) {}

  iniciarSesion(usuario: string, contrasenia: string, correo: string = ''): Observable<string> {
    return this.http.post(`${this.urlBase}/login`, { usuario, contrasenia, correo }, { responseType: 'text' });
  }

  registrarUsuario(datos: any): Observable<string> {
    return this.http.post(`${this.urlBase}/register`, datos, { responseType: 'text' });
  }

  verificarCuenta(correo: string, codigoVerificacion: string): Observable<string> {
    return this.http.post(`${this.urlBase}/verificar`, { correo, codigoVerificacion }, { responseType: 'text' });
  }

  guardarToken(token: string): void {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('clienteId');
    localStorage.removeItem('usuarioNombre');
  }

  obtenerIdDesdeToken(): number {
    const token = this.obtenerToken();
    if (!token) return 0;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.id || payload.clienteId || 0;
    } catch {
      return 0;
    }
  }

  guardarClienteId(id: number): void {
    localStorage.setItem('clienteId', String(id));
  }

  obtenerClienteId(): number {
    return Number(localStorage.getItem('clienteId')) || 0;
  }

  estaLogueado(): boolean {
    return !!this.obtenerToken();
  }
}

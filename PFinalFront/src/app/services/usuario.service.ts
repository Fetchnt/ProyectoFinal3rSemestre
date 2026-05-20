import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { UsuarioModel } from '../models/usuario.model';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {


  private http = inject(HttpClient);


  private readonly urlbase: string = 'http://localhost:8080/usuario';

  register(dto: UsuarioModel): Observable<string> {
    return this.http.post(`${this.urlbase}/register`, dto, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }

  reenviarCodigo(dto: UsuarioModel): Observable<string> {
    return this.http.post(`${this.urlbase}/reenviarcodigo`, dto, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }

  eliminarUsuario(id: number): Observable<string> {
    return this.http.delete(`${this.urlbase}/eliminarusuario/${id}`, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }
  actualizar(id: number, dto: UsuarioModel): Observable<string> {
    return this.http.put(`${this.urlbase}/actualizar/${id}`, dto, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }


  confirmarCodigo(correo: string, codigo: string): Observable<string> {

    const payload = { correo, codigoVerificacion: codigo };
    return this.http.post(`${this.urlbase}/confirmarcodigo`, payload, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }
  private manejarError(error: HttpErrorResponse) {
    let mensajeError = 'Ocurrió un error inesperado.';

    if (error.error instanceof ErrorEvent) {

      mensajeError = `Error de cliente: ${error.error.message}`;
    } else {

      const mensajeBackend = typeof error.error === 'string' ? error.error : error.message;
      mensajeError = `Error ${error.status}: ${mensajeBackend}`;
    }

    console.error('LOG DE USUARIO SERVICE:', mensajeError);
    return throwError(() => new Error(mensajeError));
  }
}



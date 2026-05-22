import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CodigoModel } from '../models/codigo.model';

@Injectable({
  providedIn: 'root',
})
export class CodigoService {


  private http = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080/codigo';

  traducirConTodas(dto: CodigoModel): Observable<CodigoModel> {
    return this.http.post<CodigoModel>(`${this.urlbase}/traducir/todas`, dto)
      .pipe(catchError(this.manejarError));
  }

  traducirConProveedor(dto: CodigoModel): Observable<CodigoModel> {
    return this.http.post<CodigoModel>(`${this.urlbase}/traducir`, dto)
      .pipe(catchError(this.manejarError));
  }

  ejecutarCodigo(dto: CodigoModel): Observable<string> {
    return this.http.post(`${this.urlbase}/ejecutar`, dto, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }

  historial(dto: CodigoModel): Observable<string> {
    return this.http.request<string>('GET', `${this.urlbase}/historial`, {
      body: dto,
      responseType: 'text' as 'json'
    }).pipe(catchError(this.manejarError));
  }

  getProveedores(): Observable<string[]> {
    return this.http.get<string[]>(`${this.urlbase}/proveedores`)
      .pipe(catchError(this.manejarError));
  }

  getLenguajes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.urlbase}/lenguajes`)
      .pipe(catchError(this.manejarError));
  }

  eliminarTraduccion(dto: CodigoModel): Observable<string> {
    return this.http.request<string>('DELETE', `${this.urlbase}/eliminartraduccion`, {
      body: dto,
      responseType: 'text' as 'json'
    }).pipe(catchError(this.manejarError));
  }

  getAll(): Observable<string> {
    return this.http.get(`${this.urlbase}/all`, { responseType: 'text' })
      .pipe(catchError(this.manejarError));
  }


  count(): Observable<number> {
    return this.http.get<number>(`${this.urlbase}/count`)
      .pipe(catchError(this.manejarError));
  }


  private manejarError(error: HttpErrorResponse) {
    let mensajeError = 'Ocurrió un error inesperado.';
    if (error.error instanceof ErrorEvent) {
      mensajeError = `Error de cliente (red/navegador): ${error.error.message}`;
    } else {
      mensajeError = `El servidor respondió con código ${error.status}: ${error.message}`;
    }
    console.error('LOG DE SERVICIO ANGULAR:', mensajeError);
    return throwError(() => new Error(mensajeError));
  }
}

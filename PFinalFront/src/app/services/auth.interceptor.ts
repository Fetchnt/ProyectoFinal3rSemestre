import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.obtenerToken();
    if (token) {
      const solicitudConToken = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
      return next.handle(solicitudConToken);
    }
    return next.handle(req);
  }
}

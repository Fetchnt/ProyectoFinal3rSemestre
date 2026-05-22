import { Component } from '@angular/core';

export interface AuditoriaEntry {
  id: number;
  usuario: string;
  accion: string;
  modulo: string;
  ip: string;
  fecha: string;
  estado: 'exitoso' | 'fallido' | 'advertencia';
}

@Component({
  selector: 'app-auditoria',
  standalone: false,
  templateUrl: './auditoria.html',
  styleUrl: './auditoria.css',
})
export class Auditoria {
  busqueda: string = '';
  filtroEstado: string = 'todos';
  filtroModulo: string = 'todos';

  registros: AuditoriaEntry[] = [
    { id: 1,  usuario: 'juan@correo.com',   accion: 'Inicio de sesión',        modulo: 'Autenticación', ip: '192.168.1.10', fecha: '20/05/2026 08:14:32', estado: 'exitoso'     },
    { id: 2,  usuario: 'maria@correo.com',  accion: 'Traducción ejecutada',     modulo: 'Traductor',     ip: '192.168.1.22', fecha: '20/05/2026 08:30:11', estado: 'exitoso'     },
    { id: 3,  usuario: 'carlos@correo.com', accion: 'Intento de login fallido', modulo: 'Autenticación', ip: '10.0.0.5',      fecha: '20/05/2026 09:02:55', estado: 'fallido'     },
    { id: 4,  usuario: 'admin',             accion: 'Usuario eliminado',        modulo: 'Admin',         ip: '127.0.0.1',    fecha: '20/05/2026 09:15:00', estado: 'exitoso'     },
    { id: 5,  usuario: 'ana@correo.com',    accion: 'Código ejecutado',         modulo: 'RunCode',       ip: '192.168.1.45', fecha: '20/05/2026 09:44:20', estado: 'exitoso'     },
    { id: 6,  usuario: 'juan@correo.com',   accion: 'Actualización de perfil',  modulo: 'Perfil',        ip: '192.168.1.10', fecha: '20/05/2026 10:05:03', estado: 'exitoso'     },
    { id: 7,  usuario: 'maria@correo.com',  accion: 'Código ejecutado',         modulo: 'RunCode',       ip: '192.168.1.22', fecha: '20/05/2026 10:22:47', estado: 'advertencia' },
    { id: 8,  usuario: 'desconocido',       accion: 'Acceso denegado',          modulo: 'Admin',         ip: '203.0.113.99', fecha: '20/05/2026 11:00:10', estado: 'fallido'     },
    { id: 9,  usuario: 'carlos@correo.com', accion: 'Registro de usuario',      modulo: 'Registro',      ip: '10.0.0.5',     fecha: '20/05/2026 11:30:00', estado: 'exitoso'     },
    { id: 10, usuario: 'ana@correo.com',    accion: 'Traducción ejecutada',     modulo: 'Traductor',     ip: '192.168.1.45', fecha: '20/05/2026 12:01:55', estado: 'exitoso'     },
    { id: 11, usuario: 'admin',             accion: 'Admin creado',             modulo: 'Admin',         ip: '127.0.0.1',    fecha: '20/05/2026 12:45:30', estado: 'exitoso'     },
    { id: 12, usuario: 'juan@correo.com',   accion: 'Cierre de sesión',         modulo: 'Autenticación', ip: '192.168.1.10', fecha: '20/05/2026 13:00:00', estado: 'exitoso'     },
  ];

  get registrosFiltrados(): AuditoriaEntry[] {
    return this.registros.filter(r => {
      const coincideBusqueda =
        !this.busqueda ||
        r.usuario.toLowerCase().includes(this.busqueda.toLowerCase()) ||
        r.accion.toLowerCase().includes(this.busqueda.toLowerCase()) ||
        r.ip.includes(this.busqueda);
      const coincideEstado = this.filtroEstado === 'todos' || r.estado === this.filtroEstado;
      const coincideModulo = this.filtroModulo === 'todos' || r.modulo === this.filtroModulo;
      return coincideBusqueda && coincideEstado && coincideModulo;
    });
  }

  get totalExitosos():     number { return this.registros.filter(r => r.estado === 'exitoso').length;     }
  get totalFallidos():     number { return this.registros.filter(r => r.estado === 'fallido').length;     }
  get totalAdvertencias(): number { return this.registros.filter(r => r.estado === 'advertencia').length; }

  get modulos(): string[] {
    return [...new Set(this.registros.map(r => r.modulo))];
  }

  badgeClass(estado: string): string {
    if (estado === 'exitoso')     return 'badge-exitoso';
    if (estado === 'fallido')     return 'badge-fallido';
    if (estado === 'advertencia') return 'badge-advertencia';
    return '';
  }

  iconoAccion(accion: string): string {
    if (accion.toLowerCase().includes('login') || accion.toLowerCase().includes('sesión')) return 'bi-box-arrow-in-right';
    if (accion.toLowerCase().includes('traducción'))  return 'bi-translate';
    if (accion.toLowerCase().includes('código'))      return 'bi-braces';
    if (accion.toLowerCase().includes('eliminado'))   return 'bi-trash';
    if (accion.toLowerCase().includes('perfil'))      return 'bi-person';
    if (accion.toLowerCase().includes('admin'))       return 'bi-shield-lock';
    if (accion.toLowerCase().includes('registro'))    return 'bi-person-plus';
    if (accion.toLowerCase().includes('denegado'))    return 'bi-ban';
    return 'bi-activity';
  }
}

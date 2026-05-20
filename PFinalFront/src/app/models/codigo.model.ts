export interface CodigoModel {
  usuarioSolicitud: string;
  lenguajeRegistro: string;
  lenguajeATraducir: string;
  proveedorIA:string;
  exitoso: boolean;
  fechaCreacion: Date;
  codigoRecibido: string;
  codigoTraducido: string;
  clienteId: number;
  inteligenciasUsadas: CodigoModel[];
}

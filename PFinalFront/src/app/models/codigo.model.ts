export interface CodigoModel {
  id?: number;
  usuarioSolicitud?: string;
  lenguajeRecibido: string;
  lenguajeATraducir: string;
  proveedorIA: string;
  exitoso?: boolean;
  fechaCreacion?: string;
  codigoRecibido: string;
  codigoTraducido?: string;
  clienteId: number;
  inteligenciasUsadas?: CodigoModel[];
}

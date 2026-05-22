package co.edu.unbosque.proyectofinal.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

public class CodigoDTO {

	private Long id;

	private String usuarioSolicitud;
	private String lenguajeRecibido;
	private String lenguajeATraducir;
	private String proveedorIA;
	private Boolean exitoso;
	private LocalDateTime fechaCreacion;
	private String codigoRecibido;
	private String codigoTraducido;
	private List<CodigoDTO> inteligenciasUsadas;
	private Long clienteId;

	public CodigoDTO() {
		super();
	}

	public CodigoDTO(String usuarioSolicitud, String lenguajeRecibido, String lenguajeATraducir, String proveedorIA,
			Boolean exitoso, LocalDateTime fechaCreacion, String codigoRecibido, String codigoTraducido,
			List<CodigoDTO> inteligenciasUsadas, Long clienteId) {
		super();
		this.usuarioSolicitud = usuarioSolicitud;
		this.lenguajeRecibido = lenguajeRecibido;
		this.lenguajeATraducir = lenguajeATraducir;
		this.proveedorIA = proveedorIA;
		this.exitoso = exitoso;
		this.fechaCreacion = fechaCreacion;
		this.codigoRecibido = codigoRecibido;
		this.codigoTraducido = codigoTraducido;
		this.inteligenciasUsadas = new ArrayList<>(inteligenciasUsadas);
		this.clienteId = clienteId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuarioSolicitud() {
		return usuarioSolicitud;
	}

	public void setUsuarioSolicitud(String usuarioSolicitud) {
		this.usuarioSolicitud = usuarioSolicitud;
	}

	public String getLenguajeRecibido() {
		return lenguajeRecibido;
	}

	public void setLenguajeRecibido(String lenguajeRecibido) {
		this.lenguajeRecibido = lenguajeRecibido;
	}

	public String getLenguajeATraducir() {
		return lenguajeATraducir;
	}

	public void setLenguajeATraducir(String lenguajeATraducir) {
		this.lenguajeATraducir = lenguajeATraducir;
	}

	public String getProveedorIA() {
		return proveedorIA;
	}

	public void setProveedorIA(String proveedorIA) {
		this.proveedorIA = proveedorIA;
	}

	public Boolean isExitoso() {
		return exitoso;
	}

	public void setExitoso(Boolean exitoso) {
		this.exitoso = exitoso;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getCodigoRecibido() {
		return codigoRecibido;
	}

	public void setCodigoRecibido(String codigoRecibido) {
		this.codigoRecibido = codigoRecibido;
	}

	public String getCodigoTraducido() {
		return codigoTraducido;
	}

	public void setCodigoTraducido(String codigoTraducido) {
		this.codigoTraducido = codigoTraducido;
	}

	public List<CodigoDTO> getInteligenciasUsadas() {
		return inteligenciasUsadas;
	}

	public void setInteligenciasUsadas(List<CodigoDTO> inteligenciasUsadas) {
		this.inteligenciasUsadas = new ArrayList<>(inteligenciasUsadas);
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	@Override
	public String toString() {
		return "CodigoDTO [id=" + id + ", usuarioSolicitud=" + usuarioSolicitud + ", lenguajeRecibido="
				+ lenguajeRecibido + ", lenguajeATraducir=" + lenguajeATraducir + ", proveedorIA=" + proveedorIA
				+ ", exitoso=" + exitoso + ", fechaCreacion=" + fechaCreacion + ", codigoRecibido=" + codigoRecibido
				+ ", codigoTraducido=" + codigoTraducido + ", inteligenciasUsadas=" + inteligenciasUsadas
				+ ", clienteId=" + clienteId + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(clienteId, codigoRecibido, codigoTraducido, exitoso, fechaCreacion, id, inteligenciasUsadas,
				lenguajeATraducir, lenguajeRecibido, proveedorIA, usuarioSolicitud);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodigoDTO other = (CodigoDTO) obj;
		return clienteId == other.clienteId && Objects.equals(codigoRecibido, other.codigoRecibido)
				&& Objects.equals(codigoTraducido, other.codigoTraducido) && exitoso == other.exitoso
				&& Objects.equals(fechaCreacion, other.fechaCreacion) && id == other.id
				&& Objects.equals(inteligenciasUsadas, other.inteligenciasUsadas)
				&& Objects.equals(lenguajeATraducir, other.lenguajeATraducir)
				&& Objects.equals(lenguajeRecibido, other.lenguajeRecibido)
				&& Objects.equals(proveedorIA, other.proveedorIA)
				&& Objects.equals(usuarioSolicitud, other.usuarioSolicitud);
	}

}
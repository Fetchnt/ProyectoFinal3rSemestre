package co.edu.unbosque.proyectofinal.entity;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "auditoria")
public class Auditoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String endpoint;
	private String metodo;
	private String usuario;
	private String descripcion;
	private int httpStatus;

	public Auditoria() {
	}

	public Auditoria(String endpoint, String metodo, String usuario, String descripcion, int httpStatus) {
		this.endpoint = endpoint;
		this.metodo = metodo;
		this.usuario = usuario;
		this.descripcion = descripcion;
		this.httpStatus = httpStatus;
	}

	public Long getId() {
		return id;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getMetodo() {
		return metodo;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	@Override
	public String toString() {
		return "Auditoria [id=" + id + ", endpoint=" + endpoint + ", metodo=" + metodo + ", usuario=" + usuario
				+ ", descripcion=" + descripcion + ", httpStatus=" + httpStatus + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, endpoint, httpStatus, id, metodo, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Auditoria other = (Auditoria) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(endpoint, other.endpoint)
				&& httpStatus == other.httpStatus && Objects.equals(id, other.id)
				&& Objects.equals(metodo, other.metodo) && Objects.equals(usuario, other.usuario);
	}

}
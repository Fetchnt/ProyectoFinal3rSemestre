package co.edu.unbosque.proyectofinal.dto;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

public class ClienteDTO {

	private long id;
	private String usuario;
	private String contrasenia;
	private String correo;
	private boolean verificado;

	public ClienteDTO() {
		super();
	}

	public ClienteDTO(long id, String usuario, String contrasenia, String correo, boolean verificado) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.correo = correo;
		this.verificado = verificado;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", correo=" + correo
				+ ", verificado=" + verificado + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, correo, usuario, verificado);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteDTO other = (ClienteDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia) && Objects.equals(correo, other.correo)
				&& Objects.equals(usuario, other.usuario) && verificado == other.verificado;
	}

}
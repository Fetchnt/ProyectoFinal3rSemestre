package co.edu.unbosque.proyectofinal.dto;

import java.util.Objects;

public class ClienteDTO {

	private long id;
	private String usuario;
	private String contrasenia;
	private String correo;

	public ClienteDTO() {
		super();
	}

	public ClienteDTO(String usuario, String contrasenia, String correo) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.correo = correo;
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
		return "ClienteDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", correo=" + correo
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, correo, id, usuario);
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
		return Objects.equals(contrasenia, other.contrasenia) && Objects.equals(correo, other.correo) && id == other.id
				&& Objects.equals(usuario, other.usuario);
	}

}

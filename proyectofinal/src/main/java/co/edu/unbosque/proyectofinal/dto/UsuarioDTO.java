package co.edu.unbosque.proyectofinal.dto;

import java.util.Objects;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UsuarioDTO {

	private Long id;
	private String usuario;
	private String contrasenia;
	@Enumerated(EnumType.STRING)
	private Rol rol;
	private String correo;
	private Boolean verificado;
	private String codigoVerificacion;

	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(String usuario, String contrasenia, Rol rol, String correo, Boolean verificado) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.rol = rol;
		this.correo = correo;
		this.verificado = verificado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Boolean isVerificado() {
		return verificado;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public String getCodigoVerificacion() {
		return codigoVerificacion;
	}

	public void setCodigoVerificacion(String codigoVerificacion) {
		this.codigoVerificacion = codigoVerificacion;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", rol=" + rol
				+ ", correo=" + correo + ", verificado=" + verificado + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, correo, id, rol, usuario, verificado);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia) && Objects.equals(correo, other.correo)
				&& Objects.equals(id, other.id) && rol == other.rol && Objects.equals(usuario, other.usuario)
				&& Objects.equals(verificado, other.verificado);
	}
}
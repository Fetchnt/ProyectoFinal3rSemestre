package co.edu.unbosque.proyectofinal.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String usuario;
	private String contrasenia;
	private String codigoAdmin;

	public Admin() {
		super();
	}

	public Admin(String usuario, String contrasenia, String codigoAdmin) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.codigoAdmin = codigoAdmin;
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

	public String getCodigoAdmin() {
		return codigoAdmin;
	}

	public void setCodigoAdmin(String codigoAdmin) {
		this.codigoAdmin = codigoAdmin;
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", codigoAdmin="
				+ codigoAdmin + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoAdmin, contrasenia, id, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Admin other = (Admin) obj;
		return Objects.equals(codigoAdmin, other.codigoAdmin) && Objects.equals(contrasenia, other.contrasenia)
				&& id == other.id && Objects.equals(usuario, other.usuario);
	}

}

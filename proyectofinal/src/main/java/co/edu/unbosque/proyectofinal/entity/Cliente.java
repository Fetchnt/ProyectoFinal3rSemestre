package co.edu.unbosque.proyectofinal.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String usuario;
	private String contrasenia;
	private String correo;
	private boolean verificado;

	// Campos para verificación por código
	private String codigoVerificacion;
	private LocalDateTime expiracionCodigo;

	// Constructores, getters y setters
	public Cliente() {
	}

	public Cliente(long id, String usuario, String contrasenia, String correo, boolean verificado,
			String codigoVerificacion, LocalDateTime expiracionCodigo) {
		this.id = id;
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.correo = correo;
		this.verificado = verificado;
		this.codigoVerificacion = codigoVerificacion;
		this.expiracionCodigo = expiracionCodigo;
	}

	// Getters y Setters (incluyendo los nuevos)
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

	public boolean isVerificado() {
		return verificado;
	}

	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}

	public String getCodigoVerificacion() {
		return codigoVerificacion;
	}

	public void setCodigoVerificacion(String codigoVerificacion) {
		this.codigoVerificacion = codigoVerificacion;
	}

	public LocalDateTime getExpiracionCodigo() {
		return expiracionCodigo;
	}

	public void setExpiracionCodigo(LocalDateTime expiracionCodigo) {
		this.expiracionCodigo = expiracionCodigo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Cliente cliente = (Cliente) o;
		return id == cliente.id && verificado == cliente.verificado && Objects.equals(usuario, cliente.usuario)
				&& Objects.equals(contrasenia, cliente.contrasenia) && Objects.equals(correo, cliente.correo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, usuario, contrasenia, correo, verificado);
	}

	@Override
	public String toString() {
		return "Cliente{" + "id=" + id + ", usuario='" + usuario + '\'' + ", correo='" + correo + '\'' + ", verificado="
				+ verificado + '}';
	}
}
package co.edu.unbosque.proyectofinal.entity;

import java.util.ArrayList;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "codigo")
public class Codigo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String usuarioSolicitud;
	private String codigoRecibido;
	private String lenguajeRecidido;
	private String lenguajeATraducir;
	private ArrayList<Codigo> inteligenciasUsadas;

	public Codigo() {
		super();
	}

	public Codigo(String usuarioSolicitud, String codigoRecibido, String lenguajeRecidido, String lenguajeATraducir,
			ArrayList<Codigo> inteligenciasUsadas) {
		super();
		this.usuarioSolicitud = usuarioSolicitud;
		this.codigoRecibido = codigoRecibido;
		this.lenguajeRecidido = lenguajeRecidido;
		this.lenguajeATraducir = lenguajeATraducir;
		this.inteligenciasUsadas = inteligenciasUsadas;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuarioSolicitud() {
		return usuarioSolicitud;
	}

	public void setUsuarioSolicitud(String usuarioSolicitud) {
		this.usuarioSolicitud = usuarioSolicitud;
	}

	public String getCodigoRecibido() {
		return codigoRecibido;
	}

	public void setCodigoRecibido(String codigoRecibido) {
		this.codigoRecibido = codigoRecibido;
	}

	public String getLenguajeRecidido() {
		return lenguajeRecidido;
	}

	public void setLenguajeRecidido(String lenguajeRecidido) {
		this.lenguajeRecidido = lenguajeRecidido;
	}

	public String getLenguajeATraducir() {
		return lenguajeATraducir;
	}

	public void setLenguajeATraducir(String lenguajeATraducir) {
		this.lenguajeATraducir = lenguajeATraducir;
	}

	public ArrayList<Codigo> getInteligenciasUsadas() {
		return inteligenciasUsadas;
	}

	public void setInteligenciasUsadas(ArrayList<Codigo> inteligenciasUsadas) {
		this.inteligenciasUsadas = inteligenciasUsadas;
	}

	@Override
	public String toString() {
		return "Codigo [id=" + id + ", usuarioSolicitud=" + usuarioSolicitud + ", codigoRecibido=" + codigoRecibido
				+ ", lenguajeRecidido=" + lenguajeRecidido + ", lenguajeATraducir=" + lenguajeATraducir
				+ ", inteligenciasUsadas=" + inteligenciasUsadas + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoRecibido, id, inteligenciasUsadas, lenguajeATraducir, lenguajeRecidido,
				usuarioSolicitud);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Codigo other = (Codigo) obj;
		return Objects.equals(codigoRecibido, other.codigoRecibido) && id == other.id
				&& Objects.equals(inteligenciasUsadas, other.inteligenciasUsadas)
				&& Objects.equals(lenguajeATraducir, other.lenguajeATraducir)
				&& Objects.equals(lenguajeRecidido, other.lenguajeRecidido)
				&& Objects.equals(usuarioSolicitud, other.usuarioSolicitud);
	}

}

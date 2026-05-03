package co.edu.unbosque.proyectofinal.dto;

import java.util.ArrayList;
import java.util.Objects;

public class CodigoDTO {

	private long id;
	private String usuarioSolicitud;
	private String codigoRecibido;
	private String lenguajeRecidido;
	private String lenguajeATraducir;
	private ArrayList<CodigoDTO> inteligenciasUsadas;

	public CodigoDTO() {
		super();
	}

	public CodigoDTO(String usuarioSolicitud, String codigoRecibido, String lenguajeRecidido, String lenguajeATraducir,
			ArrayList<CodigoDTO> inteligenciasUsadas) {
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

	public ArrayList<CodigoDTO> getInteligenciasUsadas() {
		return inteligenciasUsadas;
	}

	public void setInteligenciasUsadas(ArrayList<CodigoDTO> inteligenciasUsadas) {
		this.inteligenciasUsadas = inteligenciasUsadas;
	}

	@Override
	public String toString() {
		return "CodigoDTO [id=" + id + ", usuarioSolicitud=" + usuarioSolicitud + ", codigoRecibido=" + codigoRecibido
				+ ", lenguajeRecidido=" + lenguajeRecidido + ", lenguajeATraducir=" + lenguajeATraducir
				+ ", inteligenciasUsadas=" + inteligenciasUsadas + ", getId()=" + getId() + ", getUsuarioSolicitud()="
				+ getUsuarioSolicitud() + ", getCodigoRecibido()=" + getCodigoRecibido() + ", getLenguajeRecidido()="
				+ getLenguajeRecidido() + ", getLenguajeATraducir()=" + getLenguajeATraducir()
				+ ", getInteligenciasUsadas()=" + getInteligenciasUsadas() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
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
		CodigoDTO other = (CodigoDTO) obj;
		return Objects.equals(codigoRecibido, other.codigoRecibido) && id == other.id
				&& Objects.equals(inteligenciasUsadas, other.inteligenciasUsadas)
				&& Objects.equals(lenguajeATraducir, other.lenguajeATraducir)
				&& Objects.equals(lenguajeRecidido, other.lenguajeRecidido)
				&& Objects.equals(usuarioSolicitud, other.usuarioSolicitud);
	}

}

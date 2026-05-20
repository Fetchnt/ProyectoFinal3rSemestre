package co.edu.unbosque.proyectofinal.exception;

public class ContraseniaInvalidaException extends Exception {

	public ContraseniaInvalidaException() {
	    super("La contraseña no es válida. Debe tener al menos 12 caracteres y no estar vacía.");
	}

}

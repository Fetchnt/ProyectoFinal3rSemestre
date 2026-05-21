package co.edu.unbosque.proyectofinal.exception;

public class CorreoInvalidoException extends Exception {

	public CorreoInvalidoException() {
	    super("El correo electrónico no es válido. Debe tener un formato correcto (usuario@dominio) y el dominio debe estar entre: gmail, hotmail, outlook, yahoo, unbosque.edu.co.");
	}

}

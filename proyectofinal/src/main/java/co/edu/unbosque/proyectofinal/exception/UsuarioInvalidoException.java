package co.edu.unbosque.proyectofinal.exception;

public class UsuarioInvalidoException extends Exception {

	public UsuarioInvalidoException() {
		super("El nombre de usuario no es válido. Debe tener al menos 5 caracteres, solo letras (con o sin acentos) y espacios, sin números ni símbolos.");
	}

}

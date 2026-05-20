package co.edu.unbosque.proyectofinal.exception;

public class LanzadorExcepciones {

	public static void verifyEmail(String s) throws CorreoInvalidoException {
		if (s == null || s.isEmpty()) {
			throw new CorreoInvalidoException();
		}

		if (s.contains(" ")) {
			throw new CorreoInvalidoException();
		}

		String[] partes = s.split("@");
		if (partes.length != 2) {
			throw new CorreoInvalidoException();
		}

		String usuario = partes[0];
		String dominio = partes[1];

		if (usuario.isEmpty()) {
			throw new CorreoInvalidoException();
		}

		if (!usuario.matches("^[A-Za-z0-9.]+$")) {
			throw new CorreoInvalidoException();
		}

		if (!(dominio.contains("gmail") || dominio.contains("hotmail") || dominio.contains("outlook")
				|| dominio.contains("yahoo") || dominio.contains("unbosque.edu.co"))) {
			throw new CorreoInvalidoException();
		}

		if (!dominio.contains(".")) {
			throw new CorreoInvalidoException();
		}
	}

	public static void verifyRegisterPassword(String s) throws ContraseniaInvalidaException {
		if (s == null || s.isEmpty() || s.length() < 12) {
			throw new ContraseniaInvalidaException();
		}
	}

	public static void verifyNickname(String s) throws UsuarioInvalidoException {
		if (s == null || s.isEmpty()) {
			throw new UsuarioInvalidoException();
		}
		if (!s.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+( [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$") || s.length() < 5) {
			throw new UsuarioInvalidoException();
		}
	}
}

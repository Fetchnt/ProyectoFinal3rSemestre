package co.edu.unbosque.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectofinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectofinal.service.UsuarioService;
import co.edu.unbosque.proyectofinal.service.AuditoriaService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class UsuarioController {
	@Autowired
	private UsuarioService clienteService;
	@Autowired
	private AuditoriaService auditoriaService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UsuarioDTO dto) {
		int status = clienteService.create(dto);
		if (status == 0) {
			clienteService.enviarCorreoVerificacion(dto.getCorreo());
			auditoriaService.registrar("/usuario/register", "POST", "Registro exitoso: " + dto.getUsuario(), 201);
			return new ResponseEntity<>("Usuario registrado. Verifique su correo", HttpStatus.CREATED);
		} else if (status == 1) {
			auditoriaService.registrar("/usuario/register", "POST", "Usuario ya existe: " + dto.getUsuario(), 400);
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			auditoriaService.registrar("/usuario/register", "POST", "Correo ya registrado: " + dto.getCorreo(), 400);
			return new ResponseEntity<>("El correo ya está registrado", HttpStatus.BAD_REQUEST);
		} else {
			auditoriaService.registrar("/usuario/register", "POST", "Error al registrar", 409);
			return new ResponseEntity<>("Error al registrar usuario", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UsuarioDTO dto) {
		String result = clienteService.login(dto.getUsuario(), dto.getContrasenia());
		if (result.equals("USER_NOT_FOUND") || result.equals("WRONG_PASSWORD")) {
			auditoriaService.registrar("/usuario/login", "POST", "Login fallido: " + dto.getUsuario(), 401);
			return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
		} else if (result.equals("NOT_VERIFIED")) {
			auditoriaService.registrar("/usuario/login", "POST", "Login sin verificar: " + dto.getUsuario(), 403);
			return new ResponseEntity<>("Debe verificar su correo antes de iniciar sesión", HttpStatus.FORBIDDEN);
		} else {
			auditoriaService.registrar("/usuario/login", "POST", "Login exitoso: " + dto.getUsuario(), 200);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@PostMapping("/verificar")
	public ResponseEntity<String> verficarCorreo(@RequestBody UsuarioDTO dto) {
		boolean verificado = clienteService.confirmarCodigo(dto.getCorreo(), dto.getCodigoVerificacion());
		if (verificado) {
			auditoriaService.registrar("/usuario/verificar", "POST", "Correo verificado: " + dto.getCorreo(), 200);
			return new ResponseEntity<>("Correo verificado correctamente", HttpStatus.OK);
		} else {
			auditoriaService.registrar("/usuario/verificar", "POST", "Código inválido: " + dto.getCorreo(), 400);
			return new ResponseEntity<>("Código inválido", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/reenviarcodigo")
	public ResponseEntity<String> reenviarCorreo(@RequestBody UsuarioDTO dto) {
		boolean enviado = clienteService.enviarCorreoVerificacion(dto.getCorreo());
		if (enviado) {
			auditoriaService.registrar("/usuario/reenviarcodigo", "POST", "Código reenviado: " + dto.getCorreo(), 200);
			return new ResponseEntity<>("Código reenviado", HttpStatus.OK);
		} else {
			auditoriaService.registrar("/usuario/reenviarcodigo", "POST", "Error al reenviar: " + dto.getCorreo(), 400);
			return new ResponseEntity<>("Error al enviar correo", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/eliminarusuario")
	public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
		int status = clienteService.deleteById(id);
		if (status == 0) {
			auditoriaService.registrar("/usuario/eliminarusuario", "DELETE", "Usuario eliminado id: " + id, 200);
			return new ResponseEntity<>("Cliente eliminado", HttpStatus.OK);
		} else {
			auditoriaService.registrar("/usuario/eliminarusuario", "DELETE", "Usuario no encontrado id: " + id, 404);
			return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
		int status = clienteService.updateById(id, dto);
		if (status == 0) {
			auditoriaService.registrar("/usuario/actualizar", "PUT", "Usuario actualizado id: " + id, 200);
			return new ResponseEntity<>("Cliente actualizado", HttpStatus.OK);
		} else if (status == 1) {
			auditoriaService.registrar("/usuario/actualizar", "PUT", "Usuario no existe id: " + id, 404);
			return new ResponseEntity<>("Cliente no existe", HttpStatus.NOT_FOUND);
		} else if (status == 2) {
			auditoriaService.registrar("/usuario/actualizar", "PUT", "Usuario duplicado: " + dto.getUsuario(), 400);
			return new ResponseEntity<>("Usuario duplicado", HttpStatus.BAD_REQUEST);
		} else if (status == 3) {
			auditoriaService.registrar("/usuario/actualizar", "PUT", "Correo duplicado: " + dto.getCorreo(), 400);
			return new ResponseEntity<>("Correo duplicado", HttpStatus.BAD_REQUEST);
		} else {
			auditoriaService.registrar("/usuario/actualizar", "PUT", "Error al actualizar id: " + id, 409);
			return new ResponseEntity<>("Error al actualizar", HttpStatus.CONFLICT);
		}
	}
}
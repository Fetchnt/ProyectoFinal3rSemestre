package co.edu.unbosque.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectofinal.dto.ClienteDTO;
import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.service.ClienteService;
import co.edu.unbosque.proyectofinal.service.CodigoService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private CodigoService cService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam String correo) {
		ClienteDTO dto = new ClienteDTO();
		dto.setUsuario(usuario);
		dto.setContrasenia(contrasenia);
		dto.setCorreo(correo);
		int status = clienteService.create(dto);
		switch (status) {
		case 0:
			clienteService.enviarCorreoVerificacion(correo);
			return new ResponseEntity<>("Usuario registrado. Verifique su correo", HttpStatus.CREATED);
		case 1:
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
		case 2:
			return new ResponseEntity<>("El correo ya está registrado", HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error al registrar usuario", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contrasenia) {
		int status = clienteService.loginStatus(usuario, contrasenia);
		switch (status) {
		case 0:
			return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
		case 2:
			return new ResponseEntity<>("Debe verificar su correo antes de iniciar sesión", HttpStatus.FORBIDDEN);
		default:
			return new ResponseEntity<>("Error en login", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyEmail(@RequestParam String correo, @RequestParam String codigo) {
		boolean verificado = clienteService.confirmarCodigo(correo, codigo);
		return verificado ? new ResponseEntity<>("Correo verificado correctamente", HttpStatus.OK)
				: new ResponseEntity<>("Código inválido o expirado", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/resend")
	public ResponseEntity<String> resendCode(@RequestParam String correo) {
		boolean enviado = clienteService.enviarCorreoVerificacion(correo);
		return enviado ? new ResponseEntity<>("Código reenviado", HttpStatus.OK)
				: new ResponseEntity<>("Error al enviar correo", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/all")
	public ResponseEntity<String> getAll() {
		return new ResponseEntity<>(clienteService.getAll(), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		int status = clienteService.deleteById(id);
		return status == 0 ? new ResponseEntity<>("Cliente eliminado", HttpStatus.OK)
				: new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String correo) {
		ClienteDTO dto = new ClienteDTO();
		dto.setUsuario(usuario);
		dto.setContrasenia(contrasenia);
		dto.setCorreo(correo);
		int status = clienteService.updateById(id, dto);
		switch (status) {
		case 0:
			return new ResponseEntity<>("Cliente actualizado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("Cliente no existe", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Usuario duplicado", HttpStatus.BAD_REQUEST);
		case 3:
			return new ResponseEntity<>("Correo duplicado", HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error al actualizar", HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/count")
	public ResponseEntity<Long> count() {
		return new ResponseEntity<>(clienteService.count(), HttpStatus.OK);
	}

	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> exist(@PathVariable Long id) {
		return new ResponseEntity<>(clienteService.exist(id), HttpStatus.OK);
	}

	@PostMapping("/traducir/todas")
	@Operation(summary = "Traducir con las 3 IAs")
	public ResponseEntity<CodigoDTO> traducirConTodas(@RequestParam long clienteId, @RequestParam String codigoRecibido,
			@RequestParam String lenguajeRecibido, @RequestParam String lenguajeATraducir) {

		CodigoDTO dto = new CodigoDTO();
		dto.setClienteId(clienteId);
		dto.setUsuarioSolicitud(clienteService.obtenerUsuarioPorId(clienteId));
		dto.setCodigoRecibido(codigoRecibido);
		dto.setLenguajeRecibido(lenguajeRecibido);
		dto.setLenguajeATraducir(lenguajeATraducir);

		return new ResponseEntity<>(cService.traducirConTodasLasIAs(dto), HttpStatus.OK);
	}

	@PostMapping("/traducir")
	@Operation(summary = "Traducir con un proveedor específico")
	public ResponseEntity<CodigoDTO> traducirConProveedor(@RequestParam long clienteId,
			@RequestParam String codigoRecibido, @RequestParam String lenguajeRecibido,
			@RequestParam String lenguajeATraducir, @RequestParam String proveedor) {

		CodigoDTO dto = new CodigoDTO();
		dto.setClienteId(clienteId);
		dto.setUsuarioSolicitud(clienteService.obtenerUsuarioPorId(clienteId));
		dto.setCodigoRecibido(codigoRecibido);
		dto.setLenguajeRecibido(lenguajeRecibido);
		dto.setLenguajeATraducir(lenguajeATraducir);

		return new ResponseEntity<>(cService.traducirConProveedorEspecifico(dto, proveedor), HttpStatus.OK);
	}

	@GetMapping("/historial")
	@Operation(summary = "Ver historial de traducciones del cliente")
	public ResponseEntity<String> historial(@RequestParam long clienteId) {
		return new ResponseEntity<>(cService.getByClienteId(clienteId), HttpStatus.OK);
	}
}
package co.edu.unbosque.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.proyectofinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.service.UsuarioService;
import co.edu.unbosque.proyectofinal.service.CodigoService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class UsuarioController {

	@Autowired
	private UsuarioService clienteService;

	@Autowired
	private CodigoService cService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UsuarioDTO dto) {
		int status = clienteService.create(dto);
		if (status == 0) {
			clienteService.enviarCorreoVerificacion(dto.getCorreo());
			return new ResponseEntity<>("Usuario registrado. Verifique su correo", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("El correo ya está registrado", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Error al registrar usuario", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UsuarioDTO dto) {
		String result = clienteService.login(dto.getUsuario(), dto.getContrasenia());
		if (result.equals("USER_NOT_FOUND") || result.equals("WRONG_PASSWORD")) {
			return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
		} else if (result.equals("NOT_VERIFIED")) {
			return new ResponseEntity<>("Debe verificar su correo antes de iniciar sesión", HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyEmail(@RequestBody UsuarioDTO dto) {
		boolean verificado = clienteService.confirmarCodigo(dto.getCorreo(), dto.getCodigoVerificacion());
		if (verificado) {
			return new ResponseEntity<>("Correo verificado correctamente", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Código inválido", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/resend")
	public ResponseEntity<String> resendCode(@RequestBody UsuarioDTO dto) {
		boolean enviado = clienteService.enviarCorreoVerificacion(dto.getCorreo());
		if (enviado) {
			return new ResponseEntity<>("Código reenviado", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Error al enviar correo", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<String> getAll() {
		String lista = clienteService.getAll();
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		int status = clienteService.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Cliente eliminado", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
		int status = clienteService.updateById(id, dto);
		if (status == 0) {
			return new ResponseEntity<>("Cliente actualizado", HttpStatus.OK);
		} else if (status == 1) {
			return new ResponseEntity<>("Cliente no existe", HttpStatus.NOT_FOUND);
		} else if (status == 2) {
			return new ResponseEntity<>("Usuario duplicado", HttpStatus.BAD_REQUEST);
		} else if (status == 3) {
			return new ResponseEntity<>("Correo duplicado", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Error al actualizar", HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/count")
	public ResponseEntity<Long> count() {
		long total = clienteService.count();
		return new ResponseEntity<>(total, HttpStatus.OK);
	}

	@PostMapping("/traducir/todas")
	@Operation(summary = "Traducir con las 3 IAs")
	public ResponseEntity<CodigoDTO> traducirConTodas(@RequestBody CodigoDTO dto) {
		String nombreUsuario = clienteService.obtenerUsuarioPorId(dto.getClienteId());
		dto.setUsuarioSolicitud(nombreUsuario);
		CodigoDTO resultado = cService.traducirConTodasLasIAs(dto);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@PostMapping("/traducir")
	@Operation(summary = "Traducir con un proveedor específico")
	public ResponseEntity<CodigoDTO> traducirConProveedor(@RequestBody CodigoDTO dto) {
		String nombreUsuario = clienteService.obtenerUsuarioPorId(dto.getClienteId());
		dto.setUsuarioSolicitud(nombreUsuario);
		String proveedor = dto.getProveedorIA();
		CodigoDTO resultado = cService.traducirConProveedorEspecifico(dto, proveedor);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@GetMapping("/historial/{clienteId}")
	@Operation(summary = "Ver historial de traducciones del cliente")
	public ResponseEntity<String> historial(@PathVariable long clienteId) {
		String historial = cService.getByClienteId(clienteId);
		return new ResponseEntity<>(historial, HttpStatus.OK);
	}
}
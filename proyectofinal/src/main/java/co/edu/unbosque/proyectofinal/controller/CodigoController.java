package co.edu.unbosque.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.service.CodigoService;
import co.edu.unbosque.proyectofinal.service.Judge0Service;
import co.edu.unbosque.proyectofinal.service.AuditoriaService;
import co.edu.unbosque.proyectofinal.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/codigo")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class CodigoController {
	@Autowired
	private CodigoService codigoService;
	@Autowired
	private Judge0Service judge0Service;
	@Autowired
	private AuditoriaService auditoriaService;
	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/traducir/todas")
	public ResponseEntity<CodigoDTO> traducirConTodas(@RequestBody CodigoDTO dto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		dto.setClienteId(usuarioService.obtenerIdPorUsuario(username));
		dto.setUsuarioSolicitud(username);
		CodigoDTO resultado = codigoService.traducirConTodasLasIAs(dto);
		auditoriaService.registrar("/codigo/traducir/todas", "POST", "Traducción con todas las IAs por: " + username,
				200);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@PostMapping("/traducir")
	public ResponseEntity<CodigoDTO> traducirConProveedor(@RequestBody CodigoDTO dto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		dto.setClienteId(usuarioService.obtenerIdPorUsuario(username));
		dto.setUsuarioSolicitud(username);
		CodigoDTO resultado = codigoService.traducirConProveedorEspecifico(dto, dto.getProveedorIA());
		auditoriaService.registrar("/codigo/traducir", "POST",
				"Traducción con " + dto.getProveedorIA() + " por: " + username, 200);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@PostMapping("/ejecutar")
	@Operation(summary = "Ejecutar código con Judge0")
	public ResponseEntity<String> ejecutarCodigo(@RequestBody CodigoDTO dto) {
		if (dto.getCodigoTraducido() == null || dto.getLenguajeATraducir() == null) {
			auditoriaService.registrar("/codigo/ejecutar", "POST", "Ejecución fallida: campos nulos", 400);
			return new ResponseEntity<>("Debe proporcionar codigoTraducido y lenguajeATraducir",
					HttpStatus.BAD_REQUEST);
		}
		String resultado = judge0Service.ejecutarCodigo(dto.getCodigoTraducido(), dto.getLenguajeATraducir());
		auditoriaService.registrar("/codigo/ejecutar", "POST", "Código ejecutado en: " + dto.getLenguajeATraducir(),
				200);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@GetMapping("/historial")
	public ResponseEntity<String> historial() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Long clienteId = usuarioService.obtenerIdPorUsuario(username);
		String historial = codigoService.getByClienteId(clienteId);
		auditoriaService.registrar("/codigo/historial", "GET", "Historial consultado por: " + username, 200);
		return new ResponseEntity<>(historial, HttpStatus.OK);
	}

	@GetMapping("/proveedores")
	@Operation(summary = "Ver proveedores de IA disponibles")
	public ResponseEntity<?> proveedores() {
		auditoriaService.registrar("/codigo/proveedores", "GET", "Consulta de proveedores", 200);
		return new ResponseEntity<>(codigoService.getProveedoresDisponibles(), HttpStatus.OK);
	}

	@GetMapping("/lenguajes")
	@Operation(summary = "Ver lenguajes soportados")
	public ResponseEntity<?> lenguajes() {
		auditoriaService.registrar("/codigo/lenguajes", "GET", "Consulta de lenguajes", 200);
		return new ResponseEntity<>(codigoService.getLenguajesSoportados(), HttpStatus.OK);
	}

	@DeleteMapping("/eliminartraduccion")
	@Operation(summary = "Eliminar traducción por ID")
	public ResponseEntity<String> eliminarTraduccion(@RequestBody CodigoDTO dto) {
		int status = codigoService.deleteById(dto.getId());
		if (status == 0) {
			auditoriaService.registrar("/codigo/eliminartraduccion", "DELETE",
					"Traducción eliminada id: " + dto.getId(), 200);
			return new ResponseEntity<>("Traducción eliminada", HttpStatus.OK);
		} else {
			auditoriaService.registrar("/codigo/eliminartraduccion", "DELETE",
					"Traducción no encontrada id: " + dto.getId(), 404);
			return new ResponseEntity<>("Traducción no encontrada", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	@Operation(summary = "Ver todas las traducciones")
	public ResponseEntity<String> getAll() {
		auditoriaService.registrar("/codigo/all", "GET", "Consulta de todas las traducciones", 200);
		return new ResponseEntity<>(codigoService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/count")
	@Operation(summary = "Contar traducciones")
	public ResponseEntity<Long> count() {
		auditoriaService.registrar("/codigo/count", "GET", "Conteo de traducciones", 200);
		return new ResponseEntity<>(codigoService.count(), HttpStatus.OK);
	}
}
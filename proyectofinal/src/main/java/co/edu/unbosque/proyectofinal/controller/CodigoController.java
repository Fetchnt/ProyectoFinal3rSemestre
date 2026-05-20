package co.edu.unbosque.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.service.CodigoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/codigo")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
@Tag(name = "Traductor de Código", description = "Endpoints para traducción de código entre lenguajes")
public class CodigoController {

	@Autowired
	private CodigoService codigoService;

	/**
	 * Traduce con las 3 IAs al mismo tiempo. Body: { usuarioSolicitud,
	 * codigoRecibido, lenguajeRecidido, lenguajeATraducir }
	 */
	@PostMapping("/traducir/todas")
	@Operation(summary = "Traduce con todas las IAs disponibles", description = "Llama a Gemini, DeepSeek y Qwen y retorna los 3 resultados")
	public ResponseEntity<CodigoDTO> traducirConTodas(@RequestBody CodigoDTO dto) {
		try {
			CodigoDTO resultado = codigoService.traducirConTodasLasIAs(dto);
			return new ResponseEntity<>(resultado, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Traduce con una IA específica. Body: { usuarioSolicitud, codigoRecibido,
	 * lenguajeRecidido, lenguajeATraducir } Param: proveedor = "gemini" |
	 * "deepseek" | "qwen"
	 */
	@PostMapping("/traducir")
	@Operation(summary = "Traduce con un proveedor específico", description = "Elige entre gemini, deepseek o qwen")
	public ResponseEntity<CodigoDTO> traducirConProveedor(@RequestBody CodigoDTO dto,
			@RequestParam(defaultValue = "gemini") String proveedor) {
		try {
			CodigoDTO resultado = codigoService.traducirConProveedorEspecifico(dto, proveedor);
			return new ResponseEntity<>(resultado, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/proveedores")
	@Operation(summary = "Lista los proveedores de IA disponibles")
	public ResponseEntity<?> getProveedores() {
		return new ResponseEntity<>(codigoService.getProveedoresDisponibles(), HttpStatus.OK);
	}

	@GetMapping("/lenguajes")
	@Operation(summary = "Lista los lenguajes soportados")
	public ResponseEntity<?> getLenguajes() {
		return new ResponseEntity<>(codigoService.getLenguajesSoportados(), HttpStatus.OK);
	}

	@GetMapping("/historial")
	@Operation(summary = "Obtiene todo el historial de traducciones")
	public ResponseEntity<String> getHistorial() {
		return new ResponseEntity<>(codigoService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/historial/cliente/{clienteId}")
	@Operation(summary = "Obtiene el historial de un cliente específico")
	public ResponseEntity<String> getHistorialPorCliente(@PathVariable long clienteId) {
		return new ResponseEntity<>(codigoService.getByClienteId(clienteId), HttpStatus.OK);
	}

	@DeleteMapping("/historial/{id}")
	@Operation(summary = "Elimina una traducción del historial")
	public ResponseEntity<String> eliminar(@PathVariable Long id) {
		int resultado = codigoService.deleteById(id);
		if (resultado == 0) {
			return new ResponseEntity<>("Eliminado correctamente", HttpStatus.OK);
		} else if (resultado == 1) {
			return new ResponseEntity<>("No encontrado", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Error al eliminar", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/historial/count")
	@Operation(summary = "Cuenta el total de traducciones guardadas")
	public ResponseEntity<Long> count() {
		return new ResponseEntity<>(codigoService.count(), HttpStatus.OK);
	}
}
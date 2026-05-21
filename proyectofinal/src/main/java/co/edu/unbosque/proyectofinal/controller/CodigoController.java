package co.edu.unbosque.proyectofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.service.CodigoService;
import co.edu.unbosque.proyectofinal.service.Judge0Service;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/codigo")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class CodigoController {

	@Autowired
	private CodigoService codigoService;

	@Autowired
	private Judge0Service judge0Service;

	@PostMapping("/traducir/todas")
	public ResponseEntity<CodigoDTO> traducirConTodas(@RequestBody CodigoDTO dto) {
		String nombreUsuario = codigoService.getByClienteId(dto.getClienteId());
		dto.setUsuarioSolicitud(nombreUsuario);
		CodigoDTO resultado = codigoService.traducirConTodasLasIAs(dto);
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@PostMapping("/traducir")
	public ResponseEntity<CodigoDTO> traducirConProveedor(@RequestBody CodigoDTO dto) {
		String nombreUsuario = codigoService.getByClienteId(dto.getClienteId());
		dto.setUsuarioSolicitud(nombreUsuario);
		CodigoDTO resultado = codigoService.traducirConProveedorEspecifico(dto, dto.getProveedorIA());
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@PostMapping("/ejecutar")
	@Operation(summary = "Ejecutar código con Judge0")
	public ResponseEntity<String> ejecutarCodigo(@RequestBody CodigoDTO dto) {
		if (dto.getCodigoTraducido() == null || dto.getLenguajeATraducir() == null) {
			return new ResponseEntity<>("Debe proporcionar codigoTraducido y lenguajeATraducir",
					HttpStatus.BAD_REQUEST);
		}
		String resultado = judge0Service.ejecutarCodigo(dto.getCodigoTraducido(), dto.getLenguajeATraducir());
		return new ResponseEntity<>(resultado, HttpStatus.OK);
	}

	@GetMapping("/historial")
	public ResponseEntity<String> historial(@RequestBody CodigoDTO dto) {
		String historial = codigoService.getByClienteId(dto.getClienteId());
		return new ResponseEntity<>(historial, HttpStatus.OK);
	}

	@GetMapping("/proveedores")
	@Operation(summary = "Ver proveedores de IA disponibles")
	public ResponseEntity<?> proveedores() {
		return new ResponseEntity<>(codigoService.getProveedoresDisponibles(), HttpStatus.OK);
	}

	@GetMapping("/lenguajes")
	@Operation(summary = "Ver lenguajes soportados")
	public ResponseEntity<?> lenguajes() {
		return new ResponseEntity<>(codigoService.getLenguajesSoportados(), HttpStatus.OK);
	}

	@DeleteMapping("/eliminartraduccion")
	@Operation(summary = "Eliminar traducción por ID")
	public ResponseEntity<String> eliminarTraduccion(@RequestBody CodigoDTO dto) {
		int status = codigoService.deleteById(dto.getId());
		if (status == 0) {
			return new ResponseEntity<>("Traducción eliminada", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Traducción no encontrada", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	@Operation(summary = "Ver todas las traducciones")
	public ResponseEntity<String> getAll() {
		String lista = codigoService.getAll();
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@GetMapping("/count")
	@Operation(summary = "Contar traducciones")
	public ResponseEntity<Long> count() {
		long total = codigoService.count();
		return new ResponseEntity<>(total, HttpStatus.OK);
	}
}
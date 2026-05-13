package co.edu.unbosque.proyectofinal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.entity.Codigo;
import co.edu.unbosque.proyectofinal.repository.CodigoRepository;
import co.edu.unbosque.proyectofinal.service.ai.AiProvider;
import co.edu.unbosque.proyectofinal.service.ai.DeepSeekProvider;
import co.edu.unbosque.proyectofinal.service.ai.GeminiProvider;
import co.edu.unbosque.proyectofinal.service.ai.GroqProvider;
import co.edu.unbosque.proyectofinal.service.ai.MistralProvider;
import co.edu.unbosque.proyectofinal.service.ai.NvidiaProvider;
import co.edu.unbosque.proyectofinal.service.ai.QwenProvider;

@Service
public class CodigoService implements CRUDOPERATION<CodigoDTO> {

	@Autowired
	private CodigoRepository codigoRepository;
	@Autowired
	private GeminiProvider geminiProvider;
	@Autowired
	private DeepSeekProvider deepSeekProvider;
	@Autowired
	private QwenProvider qwenProvider;
	@Autowired
	private GroqProvider groqProvider;
	@Autowired
	private NvidiaProvider nvidiaProvider;
	@Autowired
	private MistralProvider mistralProvider;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private Gson gson;

	// ─────────────────────────────────────────────
	// TRADUCCIÓN
	// ─────────────────────────────────────────────

	public CodigoDTO traducirConTodasLasIAs(CodigoDTO dto) {
		ArrayList<CodigoDTO> resultados = new ArrayList<>();
		resultados.add(llamarProveedor(dto, geminiProvider));
		resultados.add(llamarProveedor(dto, deepSeekProvider));
		resultados.add(llamarProveedor(dto, qwenProvider));
		resultados.add(llamarProveedor(dto, groqProvider));
		resultados.add(llamarProveedor(dto, nvidiaProvider));
		resultados.add(llamarProveedor(dto, mistralProvider));
		dto.setInteligenciasUsadas(resultados);
		return dto;
	}

	public CodigoDTO traducirConProveedorEspecifico(CodigoDTO dto, String nombreProveedor) {

		AiProvider proveedorSeleccionado = null;

		if (nombreProveedor == null || nombreProveedor.isBlank()) {
			proveedorSeleccionado = geminiProvider;
		} else if (nombreProveedor.equalsIgnoreCase("deepseek")) {
			proveedorSeleccionado = deepSeekProvider;
		} else if (nombreProveedor.equalsIgnoreCase("qwen")) {
			proveedorSeleccionado = qwenProvider;
		} else if (nombreProveedor.equalsIgnoreCase("gemini")) {
			proveedorSeleccionado = geminiProvider;
		} else if (nombreProveedor.equalsIgnoreCase("groq")) {
			proveedorSeleccionado = groqProvider;
		}else if(nombreProveedor.equalsIgnoreCase("nvidia")) {
			proveedorSeleccionado = nvidiaProvider;
		}else if(nombreProveedor.equalsIgnoreCase("mistral")) {
			proveedorSeleccionado = mistralProvider;
		}
		else {
			CodigoDTO error = new CodigoDTO();
			error.setUsuarioSolicitud("sistema");
			error.setCodigoRecibido("Proveedor no reconocido: " + nombreProveedor);
			ArrayList<CodigoDTO> listaError = new ArrayList<>();
			listaError.add(error);
			dto.setInteligenciasUsadas(listaError);
			return dto;
		}

		ArrayList<CodigoDTO> resultado = new ArrayList<>();
		resultado.add(llamarProveedor(dto, proveedorSeleccionado));
		dto.setInteligenciasUsadas(resultado);
		return dto;
	}

	private CodigoDTO llamarProveedor(CodigoDTO dto, AiProvider provider) {
		CodigoDTO resultado = new CodigoDTO();
		resultado.setLenguajeRecibido(dto.getLenguajeRecibido());
		resultado.setLenguajeATraducir(dto.getLenguajeATraducir());
		resultado.setUsuarioSolicitud(provider.getNombre());

		try {
			String traduccion = provider.traducir(dto.getCodigoRecibido(), dto.getLenguajeRecibido(),
					dto.getLenguajeATraducir());
			resultado.setCodigoRecibido(traduccion);

			Codigo entidad = new Codigo();
			entidad.setUsuarioSolicitud(dto.getUsuarioSolicitud());
			entidad.setCodigoRecibido(dto.getCodigoRecibido());
			entidad.setLenguajeRecibido(dto.getLenguajeRecibido());
			entidad.setLenguajeATraducir(dto.getLenguajeATraducir());
			entidad.setCodigoTraducido(traduccion);
			entidad.setProveedorIA(provider.getNombre());
			entidad.setExitoso(true);
			entidad.setFechaCreacion(LocalDateTime.now());
			codigoRepository.save(entidad);

		} catch (Exception e) {
			resultado.setCodigoRecibido("Error: " + e.getMessage());
		}

		return resultado;
	}

	public List<String> getProveedoresDisponibles() {
		return List.of(geminiProvider.getNombre(), deepSeekProvider.getNombre(), qwenProvider.getNombre());
	}

	public List<String> getLenguajesSoportados() {
		return List.of("C++", "Python", "Java", "JavaScript", "TypeScript", "C#", "Go", "Rust", "Kotlin", "PHP");
	}

	@Override
	public int create(CodigoDTO data) {
		codigoRepository.save(mapper.map(data, Codigo.class));
		return 0;
	}

	@Override
	public String getAll() {
		return gson.toJson(codigoRepository.findAll());
	}

	public String getByClienteId(long clienteId) {
		return gson.toJson(codigoRepository.findByClienteId(clienteId));
	}

	@Override
	public int deleteById(Long id) {
		if (!codigoRepository.existsById(id))
			return 1;
		codigoRepository.deleteById(id);
		return 0;
	}

	@Override
	public long count() {
		return codigoRepository.count();
	}

	@Override
	public boolean exist(Long id) {
		return codigoRepository.existsById(id);
	}

	@Override
	public int updateById(Long id, CodigoDTO data) {
		if (!codigoRepository.existsById(id))
			return 1;
		Codigo entity = mapper.map(data, Codigo.class);
		entity.setId(id);
		codigoRepository.save(entity);
		return 0;
	}
}
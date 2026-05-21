package co.edu.unbosque.proyectofinal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.proyectofinal.ai.AiProvider;
import co.edu.unbosque.proyectofinal.ai.MistralProvider;
import co.edu.unbosque.proyectofinal.ai.NvidiaProvider;
import co.edu.unbosque.proyectofinal.ai.GroqProvider;
import co.edu.unbosque.proyectofinal.dto.CodigoDTO;
import co.edu.unbosque.proyectofinal.entity.Codigo;
import co.edu.unbosque.proyectofinal.repository.CodigoRepository;

@Service
public class CodigoService implements CRUDOPERATION<CodigoDTO> {

	@Autowired
	private CodigoRepository codigoRepository;
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

	public CodigoDTO traducirConTodasLasIAs(CodigoDTO dto) {
		ArrayList<CodigoDTO> resultados = new ArrayList<>();
		resultados.add(llamarProveedor(dto, groqProvider));
		resultados.add(llamarProveedor(dto, nvidiaProvider));
		resultados.add(llamarProveedor(dto, mistralProvider));
		dto.setInteligenciasUsadas(resultados);
		
		return dto;
	}

	public CodigoDTO traducirConProveedorEspecifico(CodigoDTO dto, String nombreProveedor) {

		AiProvider proveedorSeleccionado = null;

		if (nombreProveedor == null || nombreProveedor.isBlank()) {
			proveedorSeleccionado = mistralProvider;
		} else if (nombreProveedor.equalsIgnoreCase("groq")) {
			proveedorSeleccionado = groqProvider;
		} else if (nombreProveedor.equalsIgnoreCase("nvidia")) {
			proveedorSeleccionado = nvidiaProvider;
		} else if (nombreProveedor.equalsIgnoreCase("mistral")) {
			proveedorSeleccionado = mistralProvider;
		} else {
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
		return List.of(groqProvider.getNombre(), nvidiaProvider.getNombre(), mistralProvider.getNombre());
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
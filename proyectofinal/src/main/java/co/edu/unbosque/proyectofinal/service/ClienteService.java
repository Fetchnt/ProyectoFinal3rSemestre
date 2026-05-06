package co.edu.unbosque.proyectofinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.proyectofinal.entity.Cliente;
import co.edu.unbosque.proyectofinal.repository.ClienteRepository;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;

@Service
public class ClienteService implements CRUDOPERATION<ClienteDTO> {

	@Autowired
	private ClienteRepository cRep;

	@Autowired
	private ModelMapper mapper;

	@Override
	public int create(ClienteDTO data) {
		Optional<Cliente> encontrado = cRep.findByUsuario(data.getUsuario());
		Cliente entity = mapper.map(data, Cliente.class);
		cRep.save(entity);
		return 0;
	}

	@Override
	public String getAll() {
		Gson gson = new Gson();
		List<Cliente> entityList = (List<Cliente>) cRep.findAll();
		List<ClienteDTO> dtoList = new ArrayList<>();
		entityList.forEach(entity -> dtoList.add(mapper.map(entity, ClienteDTO.class)));
		return gson.toJson(dtoList);
	}

	@Override
	public int deleteById(Long id) {
		if (cRep.existsById(id)) {
			cRep.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	public long count() {
		return cRep.count();
	}

	@Override
	public boolean exist(Long id) {
		return cRep.existsById(id);
	}

	@Override
	public int updateById(Long id, ClienteDTO data) {
		Optional<Cliente> encontradoID = cRep.findById(id);
		Optional<Cliente> encontradoUsuario = cRep.findByUsuario(data.getUsuario());

		if (encontradoID.isPresent()) {
			ClienteDTO temp = mapper.map(encontradoID.get(), ClienteDTO.class);
			temp.setUsuario(data.getUsuario());
			temp.setContrasenia(data.getContrasenia());
			Cliente entity = mapper.map(temp, Cliente.class);
			entity.setId(id);
			cRep.save(entity);
			return 0;
		}
		return 1;
	}
	
	public boolean verifyEmail(ClienteDTO data) {
		
		return false;
		
	}

}

package co.edu.unbosque.proyectofinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectofinal.entity.Codigo;
import co.edu.unbosque.proyectofinal.repository.CodigoRepository;

@Service
public class CodigoService implements CRUDOPERATION<Codigo>{

	@Autowired
	private CodigoRepository coRep;
	
	@Override
	public int create(Codigo data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exist(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int updateById(Long id, Codigo data) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package co.edu.unbosque.proyectofinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectofinal.entity.Admin;
import co.edu.unbosque.proyectofinal.repository.AdminRepository;

@Service
public class AdminService implements CRUDOPERATION<Admin> {

	@Autowired
	private AdminRepository aRep;
	
	@Override
	public int create(Admin data) {
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
	public int updateById(Long id, Admin data) {
		// TODO Auto-generated method stub
		return 0;
	}

}

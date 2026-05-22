package co.edu.unbosque.proyectofinal.service;

import co.edu.unbosque.proyectofinal.entity.Auditoria;
import co.edu.unbosque.proyectofinal.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

	@Autowired
	private AuditoriaRepository auditoriaRepository;

	public void registrar(String endpoint, String metodo, String descripcion, int httpStatus) {
		String usuario = "anonimo";
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
			usuario = auth.getName();
		}
		auditoriaRepository.save(new Auditoria(endpoint, metodo, usuario, descripcion, httpStatus));
	}
}
package co.edu.unbosque.proyectofinal.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.proyectofinal.entity.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findById(Long id);
	
	Optional<Usuario> findByUsuario(String usuario);

	Optional<Usuario> findByCorreo(String correo);

	boolean existsByCorreo(String correo);
}
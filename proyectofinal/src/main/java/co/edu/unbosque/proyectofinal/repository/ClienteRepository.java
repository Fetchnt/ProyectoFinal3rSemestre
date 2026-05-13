package co.edu.unbosque.proyectofinal.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.proyectofinal.entity.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

	Optional<Cliente> findByUsuario(String usuario);

	Optional<Cliente> findByCorreo(String correo);

	boolean existsByCorreo(String correo);
}
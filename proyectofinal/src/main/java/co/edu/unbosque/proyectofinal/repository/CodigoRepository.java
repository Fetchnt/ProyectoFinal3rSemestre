package co.edu.unbosque.proyectofinal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.proyectofinal.entity.Codigo;

@Repository
public interface CodigoRepository extends CrudRepository<Codigo, Long> {

	/**
	 * Busca todas las traducciones de un cliente por su id.
	 * Spring genera el SQL automáticamente por el nombre del método.
	 */
	List<Codigo> findByClienteId(long clienteId);

	/**
	 * Busca traducciones por proveedor de IA.
	 */
	List<Codigo> findByProveedorIA(String proveedorIA);

	/**
	 * Busca traducciones por usuario.
	 */
	List<Codigo> findByUsuarioSolicitud(String usuarioSolicitud);
}
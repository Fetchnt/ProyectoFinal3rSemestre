package co.edu.unbosque.proyectofinal.configuration;

import co.edu.unbosque.proyectofinal.entity.Rol;
import co.edu.unbosque.proyectofinal.entity.Usuario;
import co.edu.unbosque.proyectofinal.repository.UsuarioRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración para cargar datos iniciales en la base de datos. Crea usuarios
 * predeterminados (administrador y usuario normal) al iniciar la aplicación si estos no existen
 * previamente.
 */
@Configuration
public class LoadDatabase {
  /** Logger para registrar mensajes durante la carga de datos. */
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
  @Value("${admin.password}")
  private String passwordAdmin;
  
  @Value("${usuario.password}")
  private String passwordUsuario;
  /**
   * Inicializa la base de datos con usuarios predeterminados. Crea un usuario administrador y un
   * usuario normal si no existen.
   *
   * @param userRepo Repositorio de usuarios para acceder a la base de datos
   * @param passwordEncoder Codificador de contraseñas para encriptar las contraseñas de los
   *     usuarios
   * @return Un CommandLineRunner que se ejecuta al iniciar la aplicación
   */
  @Bean
  CommandLineRunner initDatabase(UsuarioRepository userRepo, PasswordEncoder passwordEncoder) {	 
    return args -> {
      Optional<Usuario> encontrado= userRepo.findByUsuario("admin");
      if (encontrado.isPresent()) {
        log.info("El administrador ya existe, omitiendo la creación del administrador...");
        
      } else {

        Usuario admin = new Usuario("admin1", passwordEncoder.encode(passwordAdmin), Rol.ADMIN, "admin1@codetranslate.com", true);
        userRepo.save(admin);
        log.info("Precargando usuario administrador");
      }
      Optional<Usuario> encontradoUsuario = userRepo.findByUsuario("usuario1");
      if (encontradoUsuario.isPresent()) {
        log.info("El usuario normal ya existe, omitiendo la creación del usuario normal...");
      } else {
        Usuario normalUser = new Usuario("usuario1", passwordEncoder.encode(passwordUsuario), Rol.USUARIO, "usuario1@codetranslate.com", true);
        userRepo.save(normalUser);
        log.info("Precargando usuario normal");
      }
    };
  }
}

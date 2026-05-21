package co.edu.unbosque.proyectofinal.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.proyectofinal.dto.UsuarioDTO;
import co.edu.unbosque.proyectofinal.entity.Rol;
import co.edu.unbosque.proyectofinal.entity.Usuario;
import co.edu.unbosque.proyectofinal.exception.ContraseniaInvalidaException;
import co.edu.unbosque.proyectofinal.exception.CorreoInvalidoException;
import co.edu.unbosque.proyectofinal.exception.LanzadorExcepciones;
import co.edu.unbosque.proyectofinal.exception.UsuarioInvalidoException;
import co.edu.unbosque.proyectofinal.repository.UsuarioRepository;
import co.edu.unbosque.proyectofinal.security.JwtUtil;

@Service
public class UsuarioService implements CRUDOPERATION<UsuarioDTO> {

	@Autowired
	private UsuarioRepository uRep;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuditoriaService aService;

	private Gson gson = new Gson();
	private Random random = new Random();

	private static final int CODIGO_EXPIRACION_MINUTOS = 15;

	@Override
	public int create(UsuarioDTO data) {
		try {
			LanzadorExcepciones.verifyNickname(data.getUsuario());
			LanzadorExcepciones.verifyRegisterPassword(data.getContrasenia());
			LanzadorExcepciones.verifyEmail(data.getCorreo());
		} catch (UsuarioInvalidoException e) {
			return 3;
		} catch (ContraseniaInvalidaException e) {
			return 4;
		} catch (CorreoInvalidoException e) {
			return 5;
		}

		if (uRep.findByUsuario(data.getUsuario()).isPresent()) {
			return 1;
		}
		if (uRep.existsByCorreo(data.getCorreo())) {
			return 2;
		}

		Usuario entity = mapper.map(data, Usuario.class);
		entity.setId(null);
		entity.setContrasenia(passwordEncoder.encode(data.getContrasenia()));
		entity.setVerificado(false);
		entity.setRol(Rol.USUARIO);
		uRep.save(entity);
		aService.guardar(data.getUsuario(), "create");
		return 0;
	}

	@Override
	public String getAll() {
		List<Usuario> entityList = (List<Usuario>) uRep.findAll();
		List<UsuarioDTO> dtoList = entityList.stream().map(entity -> mapper.map(entity, UsuarioDTO.class))
				.collect(Collectors.toList());
		return gson.toJson(dtoList);
	}

	@Override
	public int deleteById(Long id) {
		if (uRep.existsById(id)) {
			uRep.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	public long count() {
		return uRep.count();
	}

	@Override
	public boolean exist(Long id) {
		return uRep.existsById(id);
	}

	@Override
	public int updateById(Long id, UsuarioDTO data) {
		Optional<Usuario> encontrado = uRep.findById(id);
		if (!encontrado.isPresent()) {
			return 1;
		}

		try {
			LanzadorExcepciones.verifyNickname(data.getUsuario());
			LanzadorExcepciones.verifyRegisterPassword(data.getContrasenia());
			LanzadorExcepciones.verifyEmail(data.getCorreo());
		} catch (UsuarioInvalidoException e) {
			return 4;
		} catch (ContraseniaInvalidaException e) {
			return 5;
		} catch (CorreoInvalidoException e) {
			return 6;
		}

		Optional<Usuario> usuarioExistente = uRep.findByUsuario(data.getUsuario());
		if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(id)) {
			return 2;
		}

		Optional<Usuario> emailExistente = uRep.findByCorreo(data.getCorreo());
		if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
			return 3;
		}

		Usuario cliente = encontrado.get();
		cliente.setUsuario(data.getUsuario());
		cliente.setContrasenia(passwordEncoder.encode(data.getContrasenia()));
		cliente.setCorreo(data.getCorreo());
		
		uRep.save(cliente);
		return 0;
	}

	public String login(String usuario, String contrasenia) {
		Optional<Usuario> encontrado = uRep.findByUsuario(usuario);
		if (!encontrado.isPresent()) {
			return "USER_NOT_FOUND";
		}
		Usuario cliente = encontrado.get();
		if (!passwordEncoder.matches(contrasenia, cliente.getContrasenia())) {
			return "WRONG_PASSWORD";
		}
		if (!cliente.isVerificado()) {
			return "NOT_VERIFIED";
		}
		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(cliente.getUsuario())
				.password(cliente.getContrasenia()).roles(cliente.getRol().name()).build();
		aService.guardar(encontrado.get().getUsuario(), "login");
		return jwtUtil.generateToken(cliente);
	}

	public boolean enviarCorreoVerificacion(String correo) {
		Optional<Usuario> encontrado = uRep.findByCorreo(correo);
		if (!encontrado.isPresent()) {
			return false;
		}
		Usuario cliente = encontrado.get();
		if (cliente.isVerificado()) {
			return false;
		}
		String codigo = String.format("%06d", random.nextInt(999999));
		cliente.setCodigoVerificacion(codigo);
		uRep.save(cliente);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(correo);
		message.setSubject("Verificación de cuenta - Proyecto Final");
		message.setText("Hola " + cliente.getUsuario() + ",\n\n" + "Tu código de verificación es: " + codigo + "\n"
				+ "Este código expira en " + CODIGO_EXPIRACION_MINUTOS + " minutos.\n\n"
				+ "Ingrésalo en la aplicación para activar tu cuenta.\n\n" + "Saludos.");
		try {
			mailSender.send(message);
			aService.guardar(encontrado.get().getUsuario(), "login");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			cliente.setCodigoVerificacion(null);
			uRep.save(cliente);
			return false;
		}
	}

	public boolean confirmarCodigo(String correo, String codigo) {
		Optional<Usuario> encontrado = uRep.findByCorreo(correo);
		if (!encontrado.isPresent()) {
			return false;
		}
		Usuario cliente = encontrado.get();
		if (cliente.isVerificado()) {
			return true;
		}
		if (cliente.getCodigoVerificacion() == null) {
			return false;
		}
		if (!cliente.getCodigoVerificacion().equals(codigo)) {
			return false;
		}
		cliente.setVerificado(true);
		cliente.setCodigoVerificacion(null);
		aService.guardar(encontrado.get().getUsuario(), "login");
		uRep.save(cliente);
		return true;
	}

	public String obtenerUsuarioPorId(long id) {
		Optional<Usuario> encontrado = uRep.findById(id);
		if (encontrado.isEmpty())
			return "desconocido";
		return encontrado.get().getUsuario();
	}
}
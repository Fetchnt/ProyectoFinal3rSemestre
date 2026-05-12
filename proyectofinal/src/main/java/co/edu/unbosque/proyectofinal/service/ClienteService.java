package co.edu.unbosque.proyectofinal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.proyectofinal.dto.ClienteDTO;
import co.edu.unbosque.proyectofinal.entity.Cliente;
import co.edu.unbosque.proyectofinal.repository.ClienteRepository;

@Service
public class ClienteService implements CRUDOPERATION<ClienteDTO> {

    @Autowired
    private ClienteRepository cRep;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JavaMailSender mailSender;

    private Gson gson = new Gson();
    private Random random = new Random();

    private static final int CODIGO_EXPIRACION_MINUTOS = 15;

    @Override
    public int create(ClienteDTO data) {
        if (cRep.findByUsuario(data.getUsuario()).isPresent()) {
            return 1; // usuario existe
        }
        if (cRep.existsByCorreo(data.getCorreo())) {
            return 2; // correo existe
        }

        Cliente entity = mapper.map(data, Cliente.class);
        entity.setVerificado(false);  
        entity.setCodigoVerificacion(null);
        entity.setExpiracionCodigo(null);
        cRep.save(entity);
        return 0;
    }

    @Override
    public String getAll() {
        List<Cliente> entityList = (List<Cliente>) cRep.findAll();
        List<ClienteDTO> dtoList = entityList.stream()
                .map(entity -> mapper.map(entity, ClienteDTO.class))
                .collect(Collectors.toList());
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
        Optional<Cliente> encontrado = cRep.findById(id);
        if (!encontrado.isPresent()) {
            return 1;
        }

        // Validar usuario duplicado (excluyendo el mismo id)
        Optional<Cliente> usuarioExistente = cRep.findByUsuario(data.getUsuario());
        if (usuarioExistente.isPresent() && usuarioExistente.get().getId() != id) {
            return 2;
        }

        // Validar correo duplicado
        Optional<Cliente> emailExistente = cRep.findByCorreo(data.getCorreo());
        if (emailExistente.isPresent() && emailExistente.get().getId() != id) {
            return 3;
        }

        Cliente cliente = encontrado.get();
        cliente.setUsuario(data.getUsuario());
        cliente.setContrasenia(data.getContrasenia());
        cliente.setCorreo(data.getCorreo());
        // No se modifica verificado ni códigos en esta operación
        cRep.save(cliente);
        return 0;
    }

    // ------------------- MÉTODOS PARA LOGIN Y VERIFICACIÓN -------------------

    /**
     * Autentica un usuario y verifica si su correo está confirmado.
     * @return 0 = éxito, 1 = credenciales inválidas, 2 = correo no verificado
     */
    public int loginStatus(String usuario, String contrasenia) {
        Optional<Cliente> encontrado = cRep.findByUsuario(usuario);
        if (encontrado.isPresent()) {
            Cliente cliente = encontrado.get();
            if (cliente.getContrasenia().equals(contrasenia)) {
                return cliente.isVerificado() ? 0 : 2;
            }
        }
        return 1;
    }

    /**
     * Genera un código aleatorio de 6 dígitos, lo asigna al cliente y envía el correo.
     * @param correo dirección de email del cliente
     * @return true si se envió correctamente, false en caso contrario
     */
    public boolean enviarCorreoVerificacion(String correo) {
        Optional<Cliente> optionalCliente = cRep.findByCorreo(correo);
        if (!optionalCliente.isPresent()) {
            return false;
        }

        Cliente cliente = optionalCliente.get();
        // Generar código de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(999999));
        cliente.setCodigoVerificacion(codigo);
        cliente.setExpiracionCodigo(LocalDateTime.now().plusMinutes(CODIGO_EXPIRACION_MINUTOS));
        cRep.save(cliente);

        // Enviar email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(correo);
        message.setSubject("Verificación de cuenta - Proyecto Final");
        message.setText("Hola " + cliente.getUsuario() + ",\n\n"
                + "Tu código de verificación es: " + codigo + "\n"
                + "Este código expira en " + CODIGO_EXPIRACION_MINUTOS + " minutos.\n\n"
                + "Ingrésalo en la aplicación para activar tu cuenta.\n\n"
                + "Saludos.");
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Confirma el código de verificación y activa la cuenta.
     * @param correo email del cliente
     * @param codigo código enviado
     * @return true si el código es válido y no expiró, false en otro caso
     */
    public boolean confirmarCodigo(String correo, String codigo) {
        Optional<Cliente> optionalCliente = cRep.findByCorreo(correo);
        if (!optionalCliente.isPresent()) {
            return false;
        }
        Cliente cliente = optionalCliente.get();
        if (cliente.isVerificado()) {
            // Ya estaba verificado, pero se podría considerar como true
            return true;
        }
        if (cliente.getCodigoVerificacion() == null || cliente.getExpiracionCodigo() == null) {
            return false;
        }
        if (cliente.getCodigoVerificacion().equals(codigo) &&
                cliente.getExpiracionCodigo().isAfter(LocalDateTime.now())) {
            cliente.setVerificado(true);
            cliente.setCodigoVerificacion(null);
            cliente.setExpiracionCodigo(null);
            cRep.save(cliente);
            return true;
        }
        return false;
    }
}
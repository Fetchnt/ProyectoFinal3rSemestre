package co.edu.unbosque.proyectofinal;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.gson.Gson;

@SpringBootApplication
public class ProyectofinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectofinalApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}

}

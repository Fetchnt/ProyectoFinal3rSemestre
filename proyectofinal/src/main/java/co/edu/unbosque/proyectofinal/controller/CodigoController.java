package co.edu.unbosque.proyectofinal.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codigo")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class CodigoController {

}

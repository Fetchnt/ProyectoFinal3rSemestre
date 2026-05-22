package co.edu.unbosque.proyectofinal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class Judge0Service {

	@Value("${judge0.api.url}")
	private String apiUrl;

	@Value("${judge0.api.key}")
	private String apiKey;

	@Value("${judge0.api.host}")
	private String apiHost;

	@Value("${judge0.lenguajes}")
	private String[] lenguajesPermitidos;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	private static final java.util.Map<String, Integer> LENGUAJE_IDS = new java.util.HashMap<>();
	static {
		LENGUAJE_IDS.put("python", 71);
		LENGUAJE_IDS.put("java", 62);
		LENGUAJE_IDS.put("c++", 54);
		LENGUAJE_IDS.put("kotlin", 78);
		LENGUAJE_IDS.put("lua", 64);
		LENGUAJE_IDS.put("perl", 85);
		LENGUAJE_IDS.put("haskell", 61);
		LENGUAJE_IDS.put("lisp", 55);
		LENGUAJE_IDS.put("ruby", 72);
		LENGUAJE_IDS.put("brainfuck", 56);
		LENGUAJE_IDS.put("clojure", 86);
		LENGUAJE_IDS.put("julia", 87);
		LENGUAJE_IDS.put("assembly", 45);
	}

	public String ejecutarCodigo(String codigo, String lenguaje) {

		if (codigo == null || codigo.isBlank()) {
			return "ERROR: El código no puede estar vacío.";
		}
		if (lenguaje == null) {
			return "ERROR: El lenguaje no puede ser nulo.";
		}

		boolean lenguajeValido = Arrays.asList(lenguajesPermitidos).contains(lenguaje.toLowerCase());
		if (!lenguajeValido) {
			return "ERROR: Lenguaje no soportado: " + lenguaje;
		}

		Integer languageId = LENGUAJE_IDS.get(lenguaje.toLowerCase());
		if (languageId == null) {
			return "ERROR: Lenguaje no soportado: " + lenguaje;
		}

		try {
			String codigoBase64 = Base64.getEncoder().encodeToString(codigo.getBytes());

			JsonObject body = new JsonObject();
			body.addProperty("language_id", languageId);
			body.addProperty("source_code", codigoBase64);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(apiUrl + "/submissions?base64_encoded=true&wait=true"))
					.header("Content-Type", "application/json").header("X-RapidAPI-Key", apiKey)
					.header("X-RapidAPI-Host", apiHost).POST(HttpRequest.BodyPublishers.ofString(body.toString()))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return parsearResultado(response.body());

		} catch (Exception e) {
			return "ERROR_JUDGE0: " + e.getMessage();
		}
	}

	private String parsearResultado(String jsonRespuesta) {
		try {
			JsonObject resultado = JsonParser.parseString(jsonRespuesta).getAsJsonObject();
			StringBuilder sb = new StringBuilder();

			if (resultado.has("stdout") && !resultado.get("stdout").isJsonNull()) {
				String stdout = new String(Base64.getDecoder().decode(resultado.get("stdout").getAsString()));
				sb.append("Salida:\n").append(stdout);
			}

			if (resultado.has("stderr") && !resultado.get("stderr").isJsonNull()) {
				String stderr = new String(Base64.getDecoder().decode(resultado.get("stderr").getAsString()));
				sb.append("Errores:\n").append(stderr);
			}

			if (resultado.has("compile_output") && !resultado.get("compile_output").isJsonNull()) {
				String compileOutput = new String(
						Base64.getDecoder().decode(resultado.get("compile_output").getAsString()));
				sb.append("Compilación:\n").append(compileOutput);
			}

			if (resultado.has("status")) {
				String status = resultado.getAsJsonObject("status").get("description").getAsString();
				sb.append("Estado: ").append(status);
			}

			return sb.length() > 0 ? sb.toString() : "Sin salida";

		} catch (Exception e) {
			return "ERROR al parsear respuesta de Judge0: " + e.getMessage();
		}
	}
}
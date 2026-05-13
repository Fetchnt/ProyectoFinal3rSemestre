package co.edu.unbosque.proyectofinal.service.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service("deepseek")
public class DeepSeekProvider implements AiProvider {

	@Value("${deepseek.api.key}")
	private String apiKey;

	@Autowired
	private Gson gson;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	// ── Clases internas para construir el JSON del body ──────────────
	private static class Mensaje {
		String role;
		String content;
		Mensaje(String role, String content) {
			this.role = role;
			this.content = content;
		}
	}

	private static class DeepSeekBody {
		String model = "deepseek-chat";
		List<Mensaje> messages;
		double temperature = 0.2;
		DeepSeekBody(List<Mensaje> messages) { this.messages = messages; }
	}
	// ─────────────────────────────────────────────────────────────────

	@Override
	public String traducir(String codigo, String lenguajeOrigen, String lenguajeDestino) {
	    try {
	        DeepSeekBody body = new DeepSeekBody(List.of(
	                new Mensaje("system",
	                        "You are an expert code translator. Return ONLY the translated code, no explanations."),
	                new Mensaje("user",
	                        construirPrompt(codigo, lenguajeOrigen, lenguajeDestino))));

	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create("https://api.deepseek.com/chat/completions"))
	                .header("Content-Type", "application/json")
	                .header("Authorization", "Bearer " + apiKey)
	                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
	                .build();

	        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

	        // ← agrega aquí
	        System.out.println("DEEPSEEK STATUS: " + response.statusCode());
	        System.out.println("DEEPSEEK RESPONSE: " + response.body());

	        return extraerTexto(response.body());

	    } catch (Exception e) {
	        System.out.println("DEEPSEEK EXCEPTION: " + e.getMessage());
	        return "ERROR_DEEPSEEK: " + e.getMessage();
	    }
	}

	// choices[0].message.content
	private String extraerTexto(String jsonRespuesta) {
		try {
			JsonObject root = JsonParser.parseString(jsonRespuesta).getAsJsonObject();
			JsonArray choices = root.getAsJsonArray("choices");
			return choices.get(0).getAsJsonObject()
					.getAsJsonObject("message")
					.get("content").getAsString().trim();
		} catch (Exception e) {
			return "ERROR al parsear respuesta de DeepSeek: " + e.getMessage();
		}
	}

	private String construirPrompt(String codigo, String origen, String destino) {
		return String.format(
				"Translate this %s code to %s. Return ONLY the code:\n\n%s",
				origen, destino, codigo);
	}

	@Override
	public String getNombre() {
		return "deepseek";
	}
}
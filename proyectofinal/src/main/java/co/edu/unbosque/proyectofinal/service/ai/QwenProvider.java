package co.edu.unbosque.proyectofinal.service.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Proveedor de IA usando Qwen3-Coder via OpenRouter.
 * Gratis: tier gratuito en OpenRouter.
 * Key en: https://openrouter.ai
 */
@Service("qwen")
public class QwenProvider implements AiProvider {

	@Value("${openrouter.api.key}")
	private String apiKey;

	@Autowired
	private Gson gson;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	@Override
	public String traducir(String codigo, String lenguajeOrigen, String lenguajeDestino) {
		try {
			Map<String, Object> body = Map.of(
					"model", "qwen/qwen3-coder:free",
					"messages", List.of(
							Map.of("role", "system",
									"content", "You are an expert code translator. Return ONLY the translated code."),
							Map.of("role", "user",
									"content", construirPrompt(codigo, lenguajeOrigen, lenguajeDestino))),
					"temperature", 0.2);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer " + apiKey)
					.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println("QWEN STATUS: " + response.statusCode());
			System.out.println("QWEN RESPONSE: " + response.body());

			return extraerTexto(response.body());

		} catch (Exception e) {
			return "ERROR_QWEN: " + e.getMessage();
		}
	}

	// choices[0].message.content — mismo formato que DeepSeek
	private String extraerTexto(String jsonRespuesta) {
		try {
			JsonObject root = JsonParser.parseString(jsonRespuesta).getAsJsonObject();
			JsonArray choices = root.getAsJsonArray("choices");
			return choices.get(0).getAsJsonObject()
					.getAsJsonObject("message")
					.get("content").getAsString().trim();
		} catch (Exception e) {
			return "ERROR al parsear respuesta de Qwen: " + e.getMessage();
		}
	}

	private String construirPrompt(String codigo, String origen, String destino) {
		return String.format(
				"Translate this %s code to %s. Return ONLY the translated code:\n\n%s",
				origen, destino, codigo);
	}

	@Override
	public String getNombre() {
		return "qwen";
	}
}
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

@Service("gemini")
public class GeminiProvider implements AiProvider {

	@Value("${gemini.api.key}")
	private String apiKey;

	@Autowired
	private Gson gson;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	// ── Clases internas para construir el JSON del body ──────────────
	private static class Parte {
		String text;

		Parte(String text) {
			this.text = text;
		}
	}

	private static class Contenido {
		List<Parte> parts;

		Contenido(List<Parte> parts) {
			this.parts = parts;
		}
	}

	private static class GeminiBody {
		List<Contenido> contents;

		GeminiBody(List<Contenido> contents) {
			this.contents = contents;
		}
	}
	// ─────────────────────────────────────────────────────────────────

	@Override
	public String traducir(String codigo, String lenguajeOrigen, String lenguajeDestino) {
		try {
			String prompt = construirPrompt(codigo, lenguajeOrigen, lenguajeDestino);

			GeminiBody body = new GeminiBody(List.of(new Contenido(List.of(new Parte(prompt)))));

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
					+ apiKey;

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body))).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			// Agrega esta línea temporal para ver qué devuelve Gemini
			System.out.println("GEMINI RESPONSE: " + response.body());

			return extraerTexto(response.body());

		} catch (Exception e) {
			return "ERROR_GEMINI: " + e.getMessage();
		}
	}

	// candidates[0].content.parts[0].text
	private String extraerTexto(String jsonRespuesta) {
		try {
			JsonObject root = JsonParser.parseString(jsonRespuesta).getAsJsonObject();
			JsonArray candidates = root.getAsJsonArray("candidates");
			JsonArray parts = candidates.get(0).getAsJsonObject().getAsJsonObject("content").getAsJsonArray("parts");
			return parts.get(0).getAsJsonObject().get("text").getAsString().trim();
		} catch (Exception e) {
			return "ERROR al parsear respuesta de Gemini: " + e.getMessage();
		}
	}

	private String construirPrompt(String codigo, String origen, String destino) {
		return String.format("Translate the following %s code to %s. "
				+ "Return ONLY the translated code, no explanations, no markdown.\n\n"
				+ "### %s code:\n%s\n\n### %s code:", origen, destino, origen, codigo, destino);
	}

	@Override
	public String getNombre() {
		return "gemini";
	}
}
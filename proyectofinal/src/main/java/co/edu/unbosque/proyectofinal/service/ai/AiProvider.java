package co.edu.unbosque.proyectofinal.service.ai;

/**
 * Interfaz que define el contrato para todos los proveedores de IA.
 * Cada proveedor (Gemini, DeepSeek, Qwen) implementa este contrato.
 */
public interface AiProvider {

	/**
	 * Traduce código de un lenguaje a otro.
	 *
	 * @param codigo          el código fuente a traducir
	 * @param lenguajeOrigen  lenguaje del código recibido (ej: "C++", "Python")
	 * @param lenguajeDestino lenguaje al que se quiere traducir
	 * @return el código traducido como String
	 */
	public String traducir(String codigo, String lenguajeOrigen, String lenguajeDestino);

	/**
	 * Retorna el nombre del proveedor para identificarlo en el historial.
	 *
	 * @return nombre del proveedor (ej: "gemini", "deepseek", "qwen")
	 */
	public String getNombre();
}
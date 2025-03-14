package pds.servicios;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import pds.modelo.Etiqueta;
import pds.modelo.ToDoItem;

public class ServicioAutoEtiquetadoOpenAI implements ServicioAutoEtiquetado {
	// Reemplaza con tu clave de API de OpenAI
	private static final String API_KEY = getOpenAIKey(); 
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    
	@Override
	public Collection<Etiqueta> etiquetar(ToDoItem item, Etiqueta... etiquetasRelevantes) {
		List<String> relevantes = Stream.of(etiquetasRelevantes).map(Etiqueta::valor).collect(Collectors.toList());
		
		List<String> etiquetas = etiquetar(item.getNombre(), relevantes);
		return etiquetas.stream().map(Etiqueta::new).toList();
	}    
    
	private List<String> etiquetar(String text, List<String> etiquetasRelevantes) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            
            String prompt = "Dado el siguiente texto: '" + text + "' asigna hasta dos etiquetas relevantes. ";
            if (etiquetasRelevantes.size() > 0) {
            	prompt = prompt + "Puedes utilizar alguna de las siguientes etiquetas (pero no es obligatorio): " + String.join(", ", etiquetasRelevantes);
            }
            prompt = prompt + "\n El formato del resultado debe ser SIMPLEMENTE una lista separada por comas (por ejemplo, 'etiqueta1, etiqueta2'.";
            
            ObjectMapper objectMapper = new ObjectMapper();
            RequestBody requestBody = new RequestBody("gpt-3.5-turbo", List.of(new Message("user", prompt, null)), 50);
            
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = objectMapper.writeValueAsBytes(requestBody);
                os.write(input, 0, input.length);
            }
            
            Scanner scanner = new Scanner(connection.getInputStream(), "utf-8");
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
         
            JsonNode node = objectMapper.readTree(response);
            ArrayNode choices = (ArrayNode) node.get("choices");
            JsonNode message = choices.get(0).get("message");
            String labelResponse = message.get("content").asText();
            System.out.println("Respuesta OpenAI: " + labelResponse);
            
    
            return List.of(labelResponse.split(", "));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    private record RequestBody(String model, List<Message> messages, int max_tokens) {}
    
    private record Message(String role, String content, String refusal) {}
    
    private record ResponseBody(List<Choice> choices) {}
    
    private record Choice(int index, Message message, String logprobs, String finish_reason) {}

    private static String getOpenAIKey() {
    	String key = System.getenv("OPENAI_API_KEY");
    	if (key == null)
    		throw new IllegalStateException("Incluye una clave de OpenAI en la variable de entorno OPENAI_API_KEY");
    	return key;
    }

}

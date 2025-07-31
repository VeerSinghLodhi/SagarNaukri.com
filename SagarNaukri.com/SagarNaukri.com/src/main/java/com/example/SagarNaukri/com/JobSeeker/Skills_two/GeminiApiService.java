package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two;

//src/main/java/com/example/app/external_api/GeminiApiService.java


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiApiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiApiService.class);

    private final WebClient webClient;
    private final String geminiApiKey;

    public GeminiApiService(@Value("${gemini.api.url}") String geminiApiUrl,
                            @Value("${gemini.api.key}") String geminiApiKey,
                            WebClient.Builder webClientBuilder) {
        this.geminiApiKey = geminiApiKey;

        this.webClient = webClientBuilder.baseUrl(geminiApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

                .defaultHeader("X-goog-api-key", geminiApiKey)
                .build();
    }


    public SkillSuggestionResponse getSkillSuggestions(String skillName) {
        String prompt = "For the skill '" + skillName + "', strictly provide only a JSON object containing its 'category' (string) and 'related_skills' (array of 3-5 relevant strings). Do not include any other text or markdown outside the JSON. Example: {\"category\": \"Programming\", \"related_skills\": [\"Python\", \"Data Structures\", \"Algorithms\"]}";

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]}]}";

        logger.info("Sending request to Gemini API for skill: {}", skillName);

        Mono<String> responseMono = webClient.post()

                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class);

        try {
            String geminiResponse = responseMono.block(); // Blocking call for simplicity in this example
            logger.debug("Gemini API raw response: {}", geminiResponse);
            return parseGeminiResponse(geminiResponse);
        } catch (Exception e) {
            logger.error("Error calling Gemini API for skill '{}': {}", skillName, e.getMessage(), e);
            // Return a default or error response
            return new SkillSuggestionResponse(false, "Error fetching suggestions: " + e.getMessage(), null, new ArrayList<>());
        }
    }

    private SkillSuggestionResponse parseGeminiResponse(String geminiResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(geminiResponse);
            // Check for 'error' node in the response, which indicates an API error
            JsonNode errorNode = root.path("error");
            if (!errorNode.isMissingNode() && !errorNode.isNull()) {
                String errorMessage = errorNode.path("message").asText("Unknown API error");
                int errorCode = errorNode.path("code").asInt(500); // Default to 500 if code is not present
                logger.error("Gemini API returned error: Code={}, Message={}", errorCode, errorMessage);
                return new SkillSuggestionResponse(false, "Gemini API error: " + errorMessage, null, new ArrayList<>());
            }

            JsonNode candidate = root.path("candidates").get(0);
            JsonNode textPart = candidate.path("content").path("parts").get(0).path("text");

            if (textPart.isMissingNode() || textPart.asText().trim().isEmpty()) {
                return new SkillSuggestionResponse(false, "Gemini did not provide a text response.", null, new ArrayList<>());
            }

            String aiGeneratedText = textPart.asText().trim();
            logger.debug("Gemini AI raw generated text: {}", aiGeneratedText);

            // --- Robust JSON Extraction Logic ---
            String jsonString = aiGeneratedText;

            // 1. Try to strip common markdown code blocks (e.g., ```json ... ```)
            if (jsonString.startsWith("```json") && jsonString.endsWith("```")) {
                jsonString = jsonString.substring(7, jsonString.length() - 3).trim();
            } else if (jsonString.startsWith("```") && jsonString.endsWith("```")) { // General markdown block
                jsonString = jsonString.substring(3, jsonString.length() - 3).trim();
            }

            // 2. Aggressively find the first '{' and last '}' to extract the JSON object
            int firstBrace = jsonString.indexOf('{');
            int lastBrace = jsonString.lastIndexOf('}');

            if (firstBrace == -1 || lastBrace == -1 || lastBrace < firstBrace) {
                logger.warn("Could not find a valid JSON object within AI response: {}", aiGeneratedText);
                return new SkillSuggestionResponse(false, "AI response did not contain a valid JSON object.", null, new ArrayList<>());
            }

            jsonString = jsonString.substring(firstBrace, lastBrace + 1);
            logger.debug("Extracted JSON string from AI response: {}", jsonString);
            // --- End Robust JSON Extraction Logic ---


            // Now, try to parse the extracted JSON string
            JsonNode aiJsonNode = mapper.readTree(jsonString); // Attempt to parse the extracted text as JSON
            String category = aiJsonNode.path("category").asText("");
            List<String> relatedSkills = new ArrayList<>();
            JsonNode relatedSkillsNode = aiJsonNode.path("related_skills");
            if (relatedSkillsNode.isArray()) {
                for (JsonNode skillNode : relatedSkillsNode) {
                    relatedSkills.add(skillNode.asText());
                }
            }

            if (category.isEmpty()) {
                // If category is still empty, it means the AI either didn't provide it
                // or the parsing of the internal JSON failed for that specific field.
                return new SkillSuggestionResponse(false, "Gemini response missing category field.", null, new ArrayList<>());
            }

            return new SkillSuggestionResponse(true, "Suggestions received.", category, relatedSkills);

        } catch (Exception e) {
            logger.error("Error parsing Gemini API response. Full raw response: {}. Error: {}", geminiResponse, e.getMessage(), e);
            // Fallback for when AI doesn't return perfect JSON or unexpected format
            return new SkillSuggestionResponse(false, "Failed to parse AI response. " + e.getMessage(), null, new ArrayList<>());
        }
    }

    private String escapeJson(String text) {
        // Escapes characters for JSON string values
        return text.replace("\\", "\\\\") // Escape backslashes first
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("/", "\\/"); // Escape forward slash (optional, but good practice for some contexts)
    }
}
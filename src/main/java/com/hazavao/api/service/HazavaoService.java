package com.hazavao.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HazavaoService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public HazavaoService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getDefinition(String teny) {
        try {
            // Préparer la requête pour ChatGPT
            String prompt = String.format(
                    "Omeo ny dikanteny amin'ny teny gasy ho an'ny teny '%s'. " +
                            "Raha tsy fantatrao ny dikanteny, lazao hoe 'Tsy fantatra ny dikanteny'. " +
                            "Valio amin'ny teny gasy fotsiny, tsy mila fanazavana fanampiny.",
                    teny
            );

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    ),
                    "max_tokens", 150,
                    "temperature", 0.7
            );

            // Préparer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Envoyer la requête
            ResponseEntity<String> response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Parser la réponse
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String definition = jsonResponse
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText()
                    .trim();

            log.info("Définition trouvée pour '{}': {}", teny, definition);
            return definition;

        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la définition pour '{}': {}", teny, e.getMessage());
            throw new RuntimeException("Erreur lors de l'appel à l'API ChatGPT: " + e.getMessage());
        }
    }
}
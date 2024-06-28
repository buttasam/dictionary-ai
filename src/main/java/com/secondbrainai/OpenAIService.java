package com.secondbrainai;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.model.PromptModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
public class OpenAIService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Client client = ClientBuilder.newClient();
    private final WebTarget openAIWebTarget;
    private final String openAIToken;

    @Inject
    public OpenAIService(@ConfigProperty(name = "openai.endpoint") String openAIApiEndpoint,
                         @ConfigProperty(name = "openai.token") String openAIToken) {
        this.openAIWebTarget = client.target(openAIApiEndpoint);
        this.openAIToken = openAIToken;
    }

    public TranslationResponse translate(String word) {
        try (Response response = openAIWebTarget
                .path("/chat/completions")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAIToken)
                .post(Entity.json(getTranslationPrompt(word)))) {
            String content = response.readEntity(JsonNode.class).at("/choices/0/message/content").textValue();
            return MAPPER.readValue(content, TranslationResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PromptModel getTranslationPrompt(String text) {
        return new PromptModel(Constants.MODEL_3_5_TURBO,
                new PromptModel.ResponseFormat("json_object"),
                List.of(new PromptModel.Message("system", Constants.Prompts.TRANSLATOR),
                        new PromptModel.Message("user", text)
                ));
    }

}

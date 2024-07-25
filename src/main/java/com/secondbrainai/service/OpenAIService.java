package com.secondbrainai.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondbrainai.model.Language;
import com.secondbrainai.model.PromptModel;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.utils.Constants;
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
import java.util.logging.Logger;

@ApplicationScoped
public class OpenAIService {

    private static Logger LOGGER = Logger.getLogger(OpenAIService.class.getName());


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

    public TranslationResponse translate(String word, Language fromLang, Language toLang) {
        record OpenAITranslationResponse(List<String> translations) {
        }

        try (Response response = openAIWebTarget
                .path("/chat/completions")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAIToken)
                .post(Entity.json(getTranslationPrompt(word, fromLang, toLang)))) {
            String content = response.readEntity(JsonNode.class).at("/choices/0/message/content").textValue();
            List<String> translations = MAPPER.readValue(content, OpenAITranslationResponse.class).translations();

            return new TranslationResponse(word, translations, fromLang, toLang);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PromptModel getTranslationPrompt(String text, Language from, Language to) {
        var model = new PromptModel(Constants.MODEL_3_5_TURBO,
                new PromptModel.ResponseFormat("json_object"),
                List.of(new PromptModel.Message("system", Constants.Prompts.translator(from, to)),
                        new PromptModel.Message("user", text)
                ));
        LOGGER.info(model.toString());
        return model;
    }

}

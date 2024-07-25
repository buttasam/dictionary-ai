package com.secondbrainai.resource;

import com.secondbrainai.model.Language;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.service.OpenAIService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/translator")
@ApplicationScoped
public class TranslatorResource {

    private final OpenAIService openAIService;

    @Inject
    public TranslatorResource(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @Path("/translate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TranslationResponse translate(TranslationRequest translationRequest) {
        return openAIService.translate(
                translationRequest.word(),
                translationRequest.fromLang(),
                translationRequest.toLang());
    }

    public record TranslationRequest(String word,
                                     Language fromLang,
                                     Language toLang) {
    }
}

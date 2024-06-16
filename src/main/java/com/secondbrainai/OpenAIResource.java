package com.secondbrainai;

import com.secondbrainai.model.TranslationResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/openai")
@ApplicationScoped
public class OpenAIResource {

    private final OpenAIService openAIService;

    @Inject
    public OpenAIResource(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @Path("/translate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TranslationResponse translate(TranslationRequest translationRequest) {
        return openAIService.translate(translationRequest.word());
    }

    public record TranslationRequest(String word) {
    }

}

package com.secondbrainai.resource;

import com.secondbrainai.dao.TranslationDao;
import com.secondbrainai.model.Language;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.service.OpenAIService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/openai")
@ApplicationScoped
public class OpenAIResource {

    private final OpenAIService openAIService;
    private final TranslationDao translationDao;

    @Inject
    public OpenAIResource(OpenAIService openAIService, TranslationDao translationDao) {
        this.openAIService = openAIService;
        this.translationDao = translationDao;
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

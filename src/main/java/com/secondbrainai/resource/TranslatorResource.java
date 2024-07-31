package com.secondbrainai.resource;

import com.secondbrainai.model.TranslationRequest;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.service.TranslationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/translator")
@ApplicationScoped
public class TranslatorResource {

    private final TranslationService translationService;

    @Inject
    public TranslatorResource(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Path("/translate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TranslationResponse translate(@NotNull @Valid TranslationRequest request) {
        return translationService.translate(request);
    }

}

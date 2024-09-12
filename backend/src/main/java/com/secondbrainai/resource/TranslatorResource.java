package com.secondbrainai.resource;

import com.secondbrainai.dao.FavoriteWordsDao;
import com.secondbrainai.model.FavoriteExistsResponse;
import com.secondbrainai.model.TranslationRequest;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.security.Secured;
import com.secondbrainai.service.TranslationService;
import com.secondbrainai.utils.SecurityUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/translator")
@ApplicationScoped
public class TranslatorResource {

    private final TranslationService translationService;
    private final FavoriteWordsDao favoriteWordsDao;

    @Inject
    public TranslatorResource(TranslationService translationService, FavoriteWordsDao favoriteWordsDao) {
        this.translationService = translationService;
        this.favoriteWordsDao = favoriteWordsDao;
    }

    @Path("/translate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TranslationResponse translate(@NotNull @Valid TranslationRequest request) {
        return translationService.translate(request);
    }

    @Secured
    @Path("/favorite")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveWordToFavorite(@Context SecurityContext sc,
                                       @NotNull @QueryParam("wordId") Integer wordId) {
        favoriteWordsDao.saveFavoriteWord(wordId, SecurityUtils.extractUserDetails(sc).getId());
        return Response.ok().build();
    }

    @Secured
    @Path("/favorite")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeWordFromFavorite(@Context SecurityContext sc,
                                           @NotNull @QueryParam("wordId") Integer wordId) {
        favoriteWordsDao.deleteFavoriteWord(wordId, SecurityUtils.extractUserDetails(sc).getId());
        return Response.ok().build();
    }

    @Secured
    @Path("/favorite")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TranslationResponse> getFavoriteWords(@Context SecurityContext sc) {
        return favoriteWordsDao.getAllFavoriteWords(SecurityUtils.extractUserDetails(sc).getId());
    }

    @Secured
    @GET
    @Path("/favorite/exists")
    @Produces(MediaType.APPLICATION_JSON)
    public FavoriteExistsResponse isFavoriteWord(@Context SecurityContext sc,
                                                 @NotNull @QueryParam("wordId") Integer wordId) {
        return new FavoriteExistsResponse(favoriteWordsDao.isFavoriteWord(
                SecurityUtils.extractUserDetails(sc).getId(), wordId));
    }

}

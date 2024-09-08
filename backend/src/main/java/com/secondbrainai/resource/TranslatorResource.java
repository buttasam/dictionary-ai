package com.secondbrainai.resource;

import com.secondbrainai.dao.FavoriteWordsDao;
import com.secondbrainai.model.FavoriteExistsResponse;
import com.secondbrainai.model.FavoriteRequest;
import com.secondbrainai.model.TranslationRequest;
import com.secondbrainai.model.TranslationResponse;
import com.secondbrainai.service.TranslationService;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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


    @Path("/favorite")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveWordToFavorite(@NotNull @Valid FavoriteRequest favoriteRequest) {
        translationService.saveWordToFavorite(favoriteRequest);
        return Response.ok().build();
    }

    @Path("/favorite")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeWordFromFavorite(@NotNull @Valid FavoriteRequest favoriteRequest) {
        translationService.deleteWordFromFavorite(favoriteRequest);
        return Response.ok().build();
    }

    @Path("/favorite")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TranslationResponse> getFavoriteWords(@NotNull @QueryParam("userId") Integer userId) {
        return favoriteWordsDao.getAllFavoriteWords(userId);
    }


    @GET
    @Path("/favorite/exists")
    @Produces(MediaType.APPLICATION_JSON)
    public FavoriteExistsResponse isFavoriteWord(@NotNull @QueryParam("userId") Integer userId,
                                                 @NotNull @QueryParam("wordId") Integer wordId) {
        return new FavoriteExistsResponse(favoriteWordsDao.isFavoriteWord(userId, wordId));
    }


}

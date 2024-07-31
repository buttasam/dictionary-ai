package com.secondbrainai.service;

import com.secondbrainai.dao.TranslationDao;
import com.secondbrainai.model.TranslationRequest;
import com.secondbrainai.model.TranslationResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TranslationService {

    private final TranslationDao translationDao;
    private final OpenAIService openAIService;

    @Inject
    public TranslationService(TranslationDao translationDao, OpenAIService openAIService) {
        this.translationDao = translationDao;
        this.openAIService = openAIService;
    }

    public TranslationResponse translate(TranslationRequest request) {
        List<String> translations = translationDao.findTranslations(request.word(), request.fromLang(), request.toLang());
        if (translations.isEmpty()) {
            TranslationResponse response = openAIService.translate(request.word(), request.fromLang(), request.toLang());
            if (!response.translations().isEmpty()) {
                translationDao.insertTranslation(request.word(), request.fromLang(), response.translations(), request.toLang());
            }
            translations = response.translations();
        }

        return new TranslationResponse(
                request.word(),
                translations,
                request.fromLang(),
                request.toLang()
        );
    }

}

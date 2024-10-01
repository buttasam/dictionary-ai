package com.dictionaryai.service;

import com.dictionaryai.dao.TranslationDao;
import com.dictionaryai.model.TranslationRequest;
import com.dictionaryai.model.TranslationResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TranslationService {

    private final TranslationDao translationDao;
    private final OpenAIService openAIService;

    @Inject
    public TranslationService(TranslationDao translationDao,
                              OpenAIService openAIService) {
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

        Integer wordId = translationDao.findWordId(request.word(), request.fromLang());
        return new TranslationResponse(
                request.word(),
                wordId,
                translations,
                request.fromLang(),
                request.toLang()
        );
    }
}

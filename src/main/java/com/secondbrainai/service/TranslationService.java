package com.secondbrainai.service;

import com.secondbrainai.dao.TranslationDao;
import com.secondbrainai.model.Language;
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


    public List<String> translate(String word) {

        // get translation from DB
        boolean exists = translationDao.wordExists(word, Language.CS);

        if (!exists) {
            TranslationResponse response = openAIService.translate(word);
        }


        // fetch from OpenAI

        // store

        return null;
    }

}

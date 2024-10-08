package com.dictionaryai.dao;

import com.dictionaryai.model.Language;

import java.util.List;

public interface TranslationDao {

    List<String> findTranslations(String word, Language fromLang, Language toLang);

    void insertTranslation(String word, Language fromLang, List<String> translations, Language toLang);

    Integer findWordId(String word, Language language);
}

package com.secondbrainai.dao;

import com.secondbrainai.model.Language;

import java.sql.Connection;
import java.util.List;

public interface TranslationDao {

    boolean wordExists(String word, Language language);

    List<String> findTranslations(String word);

    void insertTranslation(String word, Language languageFrom, List<String> translations, Language languageTo);

    int insertWord(Connection connection, String word, Language language);

}

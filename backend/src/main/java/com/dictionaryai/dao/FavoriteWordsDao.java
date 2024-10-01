package com.dictionaryai.dao;

import com.dictionaryai.model.TranslationResponse;

import java.util.List;

public interface FavoriteWordsDao {

    void saveFavoriteWord(int wordId, int userId);

    void deleteFavoriteWord(int wordId, int userId);

    List<TranslationResponse> getAllFavoriteWords(int userId);

    boolean isFavoriteWord(int userId, int wordId);
}

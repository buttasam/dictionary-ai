package com.secondbrainai.dao;

import com.secondbrainai.model.TranslationResponse;

import java.util.List;

public interface FavoriteWordsDao {

    void saveFavoriteWord(int wordId, int userId);

    List<TranslationResponse> getAllFavoriteWords(int userId);

}

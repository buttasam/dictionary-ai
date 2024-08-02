package com.secondbrainai.dao;

import java.util.List;

public interface FavoriteWordsDao {

    void saveFavoriteWord(int wordId, int userId);

    List<String> getAllFavoriteWords(int userId);

}

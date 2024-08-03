package com.secondbrainai.dao;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FavoriteWordsDaoImpl extends AbstractDao implements FavoriteWordsDao {

    @Override
    public void saveFavoriteWord(int wordId, int userId) {
        var sql = "INSERT INTO favorite_words (word_id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        executeUpdate(sql, wordId, userId);
    }

    @Override
    public List<String> getAllFavoriteWords(int userId) {
        var sql = """
                   SELECT w.word AS word FROM words w
                   JOIN favorite_words fw ON fw.word_id = w.id
                   WHERE fw.user_id = ?
                """;
        return executeQuery(sql, r -> {
            List<String> favoriteWords = new ArrayList<>();
            while (r.next()) {
                favoriteWords.add(r.getString("word"));
            }
            return favoriteWords;
        }, userId);
    }

}

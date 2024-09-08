package com.secondbrainai.dao;

import com.secondbrainai.model.Language;
import com.secondbrainai.model.TranslationResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FavoriteWordsDaoImpl extends AbstractDao implements FavoriteWordsDao {

    @Override
    public void saveFavoriteWord(int wordId, int userId) {
        var sql = "INSERT INTO favorite_words (word_id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        executeUpdate(sql, wordId, userId);
    }

    @Override
    public void deleteFavoriteWord(int wordId, int userId) {
        String sql = "DELETE FROM favorite_words WHERE word_id = ? AND user_id = ?";
        executeUpdate(sql, wordId, userId);
    }

    @Override
    public List<TranslationResponse> getAllFavoriteWords(int userId) {
        var sql = """
                SELECT fromWord.word AS wordA,
                    fromWord.id AS wordId,
                    ARRAY_AGG(toWord.word) AS trans,
                    fromWord.language AS wordLang,
                    toWord.language AS toLang
                FROM words fromWord
                     JOIN translations t ON from_id = fromWord.id
                     JOIN words toWord ON t.to_id = toWord.id
                     JOIN favorite_words fw ON fw.word_id = fromWord.id
                WHERE fw.user_id = ?
                GROUP BY wordA, wordId, wordLang, toLang
                """;
        return executeQuery(sql, r -> {
            List<TranslationResponse> favoriteWords = new ArrayList<>();
            while (r.next()) {
                favoriteWords.add(new TranslationResponse(
                        r.getString("wordA"),
                        r.getInt("wordId"),
                        Arrays.stream(((String[]) r.getArray("trans").getArray())).toList(),
                        Language.valueOf(r.getString("wordLang")),
                        Language.valueOf(r.getString("toLang"))
                ));
            }
            return favoriteWords;
        }, userId);
    }

    @Override
    public boolean isFavoriteWord(int userId, int wordId) {
        String sql = "SELECT 1 FROM favorite_words WHERE user_id = ? AND word_id = ?";
        return executeQuery(sql, ResultSet::next, userId, wordId);
    }


}

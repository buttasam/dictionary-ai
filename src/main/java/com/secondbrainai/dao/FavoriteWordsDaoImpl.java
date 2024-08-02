package com.secondbrainai.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FavoriteWordsDaoImpl implements FavoriteWordsDao {

    private final DataSource dataSource;

    @Inject
    public FavoriteWordsDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveFavoriteWord(int wordId, int userId) {
        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement("""
                    INSERT INTO favorite_words (word_id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING
                    """);
            preparedStatement.setInt(1, wordId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<String> getAllFavoriteWords(int userId) {
        try (var connection = dataSource.getConnection()) {
            var preparedStatement = connection.prepareStatement("""
                       SELECT w.word AS word FROM words w
                       JOIN favorite_words fw ON fw.word_id = w.id
                       WHERE fw.user_id = ?
                    """);
            preparedStatement.setInt(1, userId);
            var result = preparedStatement.executeQuery();
            List<String> favoriteWords = new ArrayList<>();
            while (result.next()) {
                favoriteWords.add(result.getString("word"));
            }
            return favoriteWords;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.secondbrainai.dao;

import com.secondbrainai.model.Language;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TranslationDaoImpl extends AbstractDao implements TranslationDao {

    public List<String> findTranslations(String word, Language fromLang, Language toLang) {
        String sql = """
                SELECT d_to.word as to_word FROM words d_from
                JOIN translations t ON t.from_id = d_from.id
                JOIN words d_to ON t.to_id = d_to.id
                WHERE d_from.word = ? AND d_from.language = ?::language
                AND d_to.language = ?::language;
                """;

        return executeQuery(sql, r -> {
            List<String> translations = new ArrayList<>();
            while (r.next()) {
                translations.add(r.getString("to_word"));
            }
            return translations;
        }, word, fromLang.name(), toLang.name());
    }

    public void insertTranslation(String word, Language fromLang, List<String> translations, Language toLang) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            int wordId = insertWord(connection, word, fromLang);
            List<Integer> translationIds = new ArrayList<>();
            for (String translation : translations) {
                translationIds.add(insertWord(connection, translation, toLang));
            }
            insertRelations(connection, wordId, translationIds);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer findWordId(String word, Language language) {
        return executeQuery("""
                SELECT id FROM words WHERE word = ? AND language = ?::language
                """, r -> {
            if (r.next()) {
                return r.getInt("id");
            }
            return null;
        }, word, language.name());
    }

    private int insertWord(Connection connection, String word, Language language) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM words WHERE word = ? AND language = ?::language;")) {
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, language.name());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String insertWordSQL = "INSERT INTO words(word, language) VALUES (?, ?::language)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertWordSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, language.name());
            if (preparedStatement.executeUpdate() > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
            throw new SQLException("No ID obtained");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert word", e);
        }
    }

    private void insertRelations(Connection connection, int wordId, List<Integer> translationIds) throws SQLException {
        String insertRelationSQL = "INSERT INTO translations(from_id, to_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertRelationSQL)) {
            for (Integer translationId : translationIds) {
                preparedStatement.setInt(1, wordId);
                preparedStatement.setInt(2, translationId);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

}

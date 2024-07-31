package com.secondbrainai.dao;

import com.secondbrainai.model.Language;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TranslationDaoImpl implements TranslationDao {

    private final DataSource dataSource;

    @Inject
    public TranslationDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> findTranslations(String word, Language fromLang, Language toLang) {
        List<String> translations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                             SELECT d_to.word as to_word FROM dictionary d_from
                             JOIN translation t ON t.from_id = d_from.id
                             JOIN dictionary d_to ON t.to_id = d_to.id
                             WHERE d_from.word = ? AND d_from.language = ?::language
                             AND d_to.language = ?::language;
                            """
            );

            preparedStatement.setString(1, word);
            preparedStatement.setString(2, fromLang.name());
            preparedStatement.setString(3, toLang.name());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                translations.add(result.getString("to_word"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return translations;
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

    private int insertWord(Connection connection, String word, Language language) {
        // find word
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM dictionary WHERE word = ? AND language = ?::language;")) {
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, language.name());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // or else insert
        String insertWordSQL = "INSERT INTO dictionary(word, language) VALUES (?, ?::language)";
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
        String insertRelationSQL = "INSERT INTO translation(from_id, to_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
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

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

    public boolean wordExists(String word, Language language) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT COUNT(*) AS count FROM dictionary WHERE word = ? AND language = ?::language"
            );

            preparedStatement.setString(1, word);
            preparedStatement.setString(2, language.name());
            ResultSet result = preparedStatement.executeQuery();
            result.next();

            int count = result.getInt("count");
            return count > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findTranslations(String word) {
        List<String> translations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                             SELECT d_to.word as to_word FROM dictionary d_from
                             JOIN translation t ON t.from_id = d_from.id
                             JOIN dictionary d_to ON t.to_id = d_to.id
                             WHERE d_from.word = ?;
                            """
            );

            preparedStatement.setString(1, word);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                translations.add(result.getString("to_word"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return translations;
    }

    public void insertTranslation(String word, Language languageFrom, List<String> translations, Language languageTo) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            int wordId = insertWord(connection, word, languageFrom);
            List<Integer> translationIds = insertTranslations(connection, translations, languageTo);
            insertRelations(connection, wordId, translationIds);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertWord(Connection connection, String word, Language language) {
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
            throw new RuntimeException("Failed to insert word");
        }
    }

    private List<Integer> insertTranslations(Connection connection, List<String> translations, Language language) throws SQLException {
        List<Integer> translationIds = new ArrayList<>();
        String insertTranslationSQL = "INSERT INTO dictionary(word, language) VALUES (?, ?::language)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTranslationSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (String translation : translations) {
                preparedStatement.setString(1, translation);
                preparedStatement.setString(2, language.name());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    translationIds.add(resultSet.getInt(1));
                }
            }
        }
        return translationIds;
    }

    private void insertRelations(Connection connection, int wordId, List<Integer> translationIds) throws SQLException {
        String insertRelationSQL = "INSERT INTO translation(from_id, to_id) VALUES (?, ?)";
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

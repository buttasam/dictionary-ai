package com.secondbrainai.dao;

import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {

    @Inject
    protected DataSource dataSource;

    public void executeUpdate(String sql, Object... params) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T executeQuery(String sql, ResultSetMapper<T> handler, Object... parameters) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return handler.handle(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
    }

    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T handle(ResultSet resultSet) throws SQLException;
    }
}

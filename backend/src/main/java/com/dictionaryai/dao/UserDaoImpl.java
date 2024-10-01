package com.dictionaryai.dao;

import com.dictionaryai.model.CredentialsHash;
import com.dictionaryai.security.UserDetails;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.ResultSet;
import java.util.Optional;

@ApplicationScoped
public class UserDaoImpl extends AbstractDao implements UserDao {

    @Override
    public Optional<UserDetails> findUserByAccessToken(String accessToken) {
        String sql = "SELECT id, username FROM users WHERE access_token = ?";
        return Optional.ofNullable(executeQuery(sql, resultSet -> {
            if (resultSet.next()) {
                return new UserDetails(resultSet.getInt("id"),
                        resultSet.getString("username")
                );
            }
            return null;
        }, accessToken));
    }

    @Override
    public void insertUser(String username, CredentialsHash credentialsHash) {
        String sql = "INSERT INTO users (username, password_hash, password_salt ) VALUES (?, ?, ?)";
        executeUpdate(sql, username, credentialsHash.passwordHash(), credentialsHash.salt());
    }

    @Override
    public Optional<CredentialsHash> findByUserName(String userName) {
        String sql = "SELECT password_hash, password_salt FROM users WHERE username = ?";

        return Optional.ofNullable(executeQuery(sql, resultSet -> {
            if (resultSet.next()) {
                return new CredentialsHash(
                        resultSet.getBytes("password_hash"),
                        resultSet.getBytes("password_salt"));
            }
            return null;
        }, userName));
    }

    @Override
    public void updateAccessToken(String username, String accessToken) {
        String sql = "UPDATE users SET access_token = ? WHERE username = ?";
        executeUpdate(sql, accessToken, username);
    }

    @Override
    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        return executeQuery(sql, ResultSet::next, username);
    }

}

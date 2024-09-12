package com.secondbrainai.dao;

import com.secondbrainai.model.CredentialsHash;

import java.util.Optional;

public interface UserDao {

    Optional<Integer> findUserByAccessToken(String accessToken);

    void insertUser(String username, CredentialsHash credentialsHash);

    Optional<CredentialsHash> findByUserName(String userName);

    void updateAccessToken(String username, String accessToken);

    boolean userExists(String username);
}

package com.dictionaryai.dao;

import com.dictionaryai.model.CredentialsHash;
import com.dictionaryai.security.UserDetails;

import java.util.Optional;

public interface UserDao {

    Optional<UserDetails> findUserByAccessToken(String accessToken);

    void insertUser(String username, CredentialsHash credentialsHash);

    Optional<CredentialsHash> findByUserName(String userName);

    void updateAccessToken(String username, String accessToken);

    boolean userExists(String username);
}

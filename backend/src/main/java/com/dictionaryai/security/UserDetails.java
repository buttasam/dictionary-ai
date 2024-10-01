package com.dictionaryai.security;

import java.security.Principal;

public class UserDetails implements Principal {

    private final String username;
    private final int id;

    public UserDetails(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }
}

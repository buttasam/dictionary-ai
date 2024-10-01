package com.dictionaryai.model;

public record CredentialsHash(byte[] passwordHash, byte[] salt) {
}

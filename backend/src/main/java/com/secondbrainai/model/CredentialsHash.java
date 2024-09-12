package com.secondbrainai.model;

public record CredentialsHash(byte[] passwordHash, byte[] salt) {
}

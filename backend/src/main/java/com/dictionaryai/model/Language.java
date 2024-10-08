package com.dictionaryai.model;

public enum Language {
    CS("Czech"), EN("English"), DE("German");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

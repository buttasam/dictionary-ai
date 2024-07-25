package com.secondbrainai.model;

public enum Language {
    CS("Czech"), EN("English");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

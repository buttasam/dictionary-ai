package com.secondbrainai.model;

public record TranslationRequest(String word,
                                 Language fromLang,
                                 Language toLang) {
}
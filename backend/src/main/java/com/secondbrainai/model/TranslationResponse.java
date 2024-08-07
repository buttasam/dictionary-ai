package com.secondbrainai.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record TranslationResponse(String word,
                                  @JsonInclude(JsonInclude.Include.NON_NULL) Integer wordId,
                                  List<String> translations,
                                  Language fromLang,
                                  Language toLang) {
}

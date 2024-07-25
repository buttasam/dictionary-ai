package com.secondbrainai.model;

import java.util.List;

public record TranslationResponse(String word, List<String> translations, Language fromLang, Language toLang) {
}

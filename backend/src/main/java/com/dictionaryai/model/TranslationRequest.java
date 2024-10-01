package com.dictionaryai.model;

import com.dictionaryai.model.validation.WordsLimit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TranslationRequest(@NotNull @WordsLimit @Size(min = 1, max = 200) String word,
                                 @NotNull Language fromLang,
                                 @NotNull Language toLang) {
}
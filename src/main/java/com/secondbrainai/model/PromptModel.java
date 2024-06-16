package com.secondbrainai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PromptModel(String model,
                          @JsonProperty("response_format") ResponseFormat responseFormat,
                          List<Message> messages) {

    public record Message(String role, String content) {

    }

    public record ResponseFormat(String type) {
    }

}


package com.dictionaryai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@WireMockTest(httpPort = 7777)
@HelidonTest
@AddConfig(key = "openai.endpoint", value = "http://localhost:7777")
class MainTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Inject
    private WebTarget target;

    private static final String MOCK_RESPONSE = """
                {
                  "id": "chatcmpl-9f7ietnujnTBljocuTTb6pPWIhhmP",
                  "object": "chat.completion",
                  "created": 1719588148,
                  "model": "gpt-3.5-turbo-0125",
                  "choices": [
                    {
                      "index": 0,
                      "message": {
                        "role": "assistant",
                        "content": "{\\n    \\"translations\\": [\\"postoj\\", \\"názor\\", \\"přístup\\"]\\n}"
                      },
                      "logprobs": null,
                      "finish_reason": "stop"
                    }
                  ],
                  "usage": {
                    "prompt_tokens": 42,
                    "completion_tokens": 21,
                    "total_tokens": 63
                  },
                  "system_fingerprint": null
                }
            """;

    @BeforeEach
    void setupMock() {
        // Mock the OpenAI API response
        WireMock.stubFor(WireMock.post("/chat/completions")
                .willReturn(WireMock.ok(MOCK_RESPONSE)
                        .withHeader("Content-Type", "application/json")));
    }

    @Test
    void testTranslate(WireMockRuntimeInfo wmRuntimeInfo) throws JsonProcessingException {
        String requestPayload = """
                {
                  "word": "attitude",
                  "fromLang": "EN",
                  "toLang": "CS"
                }
                """;

        Response response = target
                .path("/translator/translate")
                .request()
                .post(Entity.json(requestPayload));

        JsonNode expectedJson = JsonNodeFactory.instance.objectNode()
                .put("word", "attitude")
                .put("fromLang", "EN")
                .put("toLang", "CS")
                .set("translations", JsonNodeFactory.instance.arrayNode()
                        .add("postoj")
                        .add("názor")
                        .add("přístup")
                );
        JsonNode jsonResponse = response.readEntity(JsonNode.class);

        assertThat(response.getStatus(), is(200));

        assertThat(jsonResponse.get("word"), is(expectedJson.get("word")));
        assertThat(jsonResponse.get("fromLang"), is(expectedJson.get("fromLang")));
        assertThat(jsonResponse.get("toLang"), is(expectedJson.get("toLang")));
        assertThat(jsonResponse.get("translations"), hasItems(
                textNode("postoj"),
                textNode("názor"),
                textNode("přístup")
        ));

    }

    private Matcher<JsonNode> textNode(String value) {
        return equalTo(JsonNodeFactory.instance.textNode(value));
    }
}

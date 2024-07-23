
package com.secondbrainai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
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
    void testHealth(WireMockRuntimeInfo wmRuntimeInfo) throws JsonProcessingException {
        String requestPayload = """
            {
              "word": "for granted"
            }
            """;

        Response response = target
                .path("/openai/translate")
                .request()
                .post(Entity.json(requestPayload));

        assertThat(response.getStatus(), is(200));
        assertThat(response.readEntity(JsonNode.class), is(MAPPER.readTree("""
                {"translations": ["postoj", "názor", "přístup"]}
                """)));
    }

    @Test
    public void testTranslate() {
        target.path("/openai").request().get();
    }
}

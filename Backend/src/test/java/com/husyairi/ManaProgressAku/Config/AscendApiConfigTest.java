package com.husyairi.ManaProgressAku.Config;

import com.husyairi.ManaProgressAku.Service.impl.AscendApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AscendApiConfigTest {

    private static final String FAKE_KEY = "fake-key-for-tests";
    private static final String FAKE_HOST = "exercisedb.example.test";

    private RestTemplate restTemplateFromTheConfig() {
        AscendApiConfig config = new AscendApiConfig();
        ReflectionTestUtils.setField(config, "rapidApiKey", FAKE_KEY);
        ReflectionTestUtils.setField(config, "rapidApiHost", FAKE_HOST);
        return config.ascendApiRestTemplate(new RestTemplateBuilder());
    }

    @Test
    void everyRequestCarriesTheRapidApiKeyAndHost() {
        RestTemplate restTemplate = restTemplateFromTheConfig();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("http://localhost:9/anything"))
                .andExpect(header("x-rapidapi-key", FAKE_KEY))
                .andExpect(header("x-rapidapi-host", FAKE_HOST))
                .andRespond(withSuccess("ok", MediaType.TEXT_PLAIN));

        restTemplate.getForObject("http://localhost:9/anything", String.class);

        server.verify();
    }

    @Test
    void theClientBuiltOnThatRestTemplateSendsTheHeadersToo() {
        RestTemplate restTemplate = restTemplateFromTheConfig();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("http://localhost:9/api/v1/exercises/0001"))
                .andExpect(header("x-rapidapi-key", FAKE_KEY))
                .andExpect(header("x-rapidapi-host", FAKE_HOST))
                .andRespond(withSuccess("{\"data\": null}", MediaType.APPLICATION_JSON));

        new AscendApiClient(restTemplate, "http://localhost:9/api/v1").getExerciseById("0001");

        server.verify();
    }
}
package com.husyairi.ManaProgressAku.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AscendApiConfig {

    @Value("${rapidapi.exercisedb.key}")
    private String rapidApiKey;

    @Value("${rapidapi.exercisedb.host}")
    private String rapidApiHost;

    @Bean
    public RestTemplate ascendApiRestTemplate(RestTemplateBuilder builder){
        ClientHttpRequestInterceptor rapidApiHeaders = (request, body, execution) -> {
            request.getHeaders().add("x-rapidapi-key", rapidApiKey);
            request.getHeaders().add("x-rapidapi-host", rapidApiHost);

            return execution.execute(request, body);
        };

        return builder
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(10))
                .additionalInterceptors(rapidApiHeaders)
                .build();
    }
}

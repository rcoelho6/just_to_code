package com.example.test.just_to_code;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
@Configuration
public class JacksonMapperConfig {
    
//    @Bean
//    @Primary
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        return builder.createXmlMapper(false)
//                .failOnUnknownProperties(false)
//                .featuresToDisable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
//                .build();
//    }
}

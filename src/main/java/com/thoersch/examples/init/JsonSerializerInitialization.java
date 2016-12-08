package com.thoersch.examples.init;

import com.fasterxml.jackson.databind.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSerializerInitialization {
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public ObjectReader getObjectReader() {
        return getObjectMapper().reader();
    }

    @Bean
    public ObjectWriter getObjectWriter() {
        return getObjectMapper().writer();
    }
}

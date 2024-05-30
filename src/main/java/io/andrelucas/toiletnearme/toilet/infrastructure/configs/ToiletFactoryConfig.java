package io.andrelucas.toiletnearme.toilet.infrastructure.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToiletFactoryConfig {

    private final ObjectMapper objectMapper;

    public ToiletFactoryConfig(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public ToiletEventFactory toiletEventFactory() {
        return new ToiletEventFactory(objectMapper);
    }
}

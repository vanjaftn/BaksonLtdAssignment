package org.example.config;

import org.example.interfaces.HelloEndpoint;
import org.example.model.HelloEndpointWithRepository;
import org.example.model.HelloEndpointWithMyMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EndpointConfiguration {

    @Profile("repository")
    @Bean
    public HelloEndpoint helloEndpointWithRepository() {
        return new HelloEndpointWithRepository();
    }

    @Profile("myMemory")
    @Bean
    public HelloEndpoint helloEndpointWithMyMemory() {
        return new HelloEndpointWithMyMemory();
    }
}


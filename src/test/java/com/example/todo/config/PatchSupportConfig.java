package com.example.todo.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class PatchSupportConfig {

    @Bean
    public RestTemplateCustomizer useHttpComponents(){
        return restTemplate -> restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
}

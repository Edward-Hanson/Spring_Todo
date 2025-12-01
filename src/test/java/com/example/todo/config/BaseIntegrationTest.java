package com.example.todo.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> postgres = TestContainerConfig.getInstance();

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgres::getJdbcUrl);
        registry.add("spring.datasource.password",postgres::getPassword);
        registry.add("spring.datasource.username",postgres::getUsername);
    }
}

package com.vn.sanzee.oauth2_client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.vn.sanzee.oauth2_client.repository")
public class ReactiveRepositoryConfiguration {
}

package com.vn.sanzee.oauth2_client.repository;

import com.vn.sanzee.oauth2_client.entity.OAuth2RegisteredClientEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface Oauth2RegisteredClientRepository extends ReactiveCrudRepository<OAuth2RegisteredClientEntity, String> {
    Mono<OAuth2RegisteredClientEntity> findByRegistrationId(String registrationId);
}

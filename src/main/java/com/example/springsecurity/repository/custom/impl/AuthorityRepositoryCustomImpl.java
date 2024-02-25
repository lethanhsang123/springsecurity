package com.example.springsecurity.repository.custom.impl;

import com.example.springsecurity.repository.custom.AuthorityRepositoryCustom;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.List;

public class AuthorityRepositoryCustomImpl implements AuthorityRepositoryCustom {

    private final EntityManager entityManager;

    public AuthorityRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Integer> findAuthorityIdsByNames(Collection<String> names) {
        return null;
    }
}

package com.example.springsecurity.repository.custom.impl;

import com.example.springsecurity.entity.Role;
import com.example.springsecurity.repository.custom.RoleRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Collection;
import java.util.List;

public class RoleRepositoryCustomImpl implements RoleRepositoryCustom {

    private final EntityManager entityManager;

    public RoleRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Role> findAllByRole(Role role) {
        Query query = entityManager.createQuery("");
        return query.getResultList();
    }
}

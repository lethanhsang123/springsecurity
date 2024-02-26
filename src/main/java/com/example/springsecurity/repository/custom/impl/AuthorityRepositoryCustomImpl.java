package com.example.springsecurity.repository.custom.impl;

import com.example.springsecurity.entity.Authority;
import com.example.springsecurity.repository.custom.AuthorityRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorityRepositoryCustomImpl implements AuthorityRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    public AuthorityRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Authority> findAllByAuthority(Authority authority) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Authority> query = criteriaBuilder.createQuery(Authority.class);
        Root<Authority> root = query.from(Authority.class);
        query.select(root).where(buildPredicates(authority, criteriaBuilder, root));
        return entityManager.createQuery(query).getResultList();
    }

    private Predicate[] buildPredicates(Authority authority, CriteriaBuilder criteriaBuilder, Root<Authority> root) {
        List<Predicate> predicates = new ArrayList<>();
//        root.fetch(""); fetch nested object
        if (Objects.nonNull(authority.getId())) {
            predicates.add(criteriaBuilder.equal(root.get("id"), authority.getId()));
        }
        if (Objects.nonNull(authority.getName())) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" +authority.getName()+ "%"));
        }

        return predicates.toArray(new Predicate[0]);
    }
}

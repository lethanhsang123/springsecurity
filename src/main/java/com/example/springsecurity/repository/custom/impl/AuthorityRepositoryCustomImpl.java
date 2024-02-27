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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuthorityRepositoryCustomImpl implements AuthorityRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    public AuthorityRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Authority> findAllByNames(List<String> names) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Authority> query = criteriaBuilder.createQuery(Authority.class);
        Root<Authority> root = query.from(Authority.class);
        query.select(root).where(buildPredicates(names, criteriaBuilder, root));
        return entityManager.createQuery(query).getResultList();
    }

    private Predicate[] buildPredicates(List<String> names, CriteriaBuilder criteriaBuilder, Root<Authority> root) {
        List<Predicate> predicates = new ArrayList<>();
//        root.fetch(""); fetch nested object
        if (Objects.nonNull(names)) {
            Predicate[] predicatesTemp = new Predicate[names.size()];
            for (int i = 0; i < names.size(); i++) {
                predicatesTemp[i] = criteriaBuilder.equal(root.get("name"), names.get(i));
            }
            predicates.add(criteriaBuilder.or(predicatesTemp));
        }
        return predicates.toArray(new Predicate[0]);
    }
}

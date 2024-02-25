package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Authority;
import com.example.springsecurity.repository.custom.AuthorityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuthorityRepository extends AuthorityRepositoryCustom, JpaRepository<Authority, Integer> {
    List<Authority> findAllByName(Collection<String> names);
}

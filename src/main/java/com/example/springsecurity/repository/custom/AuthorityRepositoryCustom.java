package com.example.springsecurity.repository.custom;

import com.example.springsecurity.entity.Authority;

import java.util.Collection;
import java.util.List;

public interface AuthorityRepositoryCustom {
    List<Authority> findAllByAuthority(Authority authority);
}

package com.example.authorizationserver.repositories.custom;


import com.example.authorizationserver.entities.Authority;

import java.util.List;

public interface AuthorityRepositoryCustom {
    List<Authority> findAllByNames(List<String> names);
}

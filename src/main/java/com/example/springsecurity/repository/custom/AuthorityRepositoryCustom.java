package com.example.springsecurity.repository.custom;

import java.util.Collection;
import java.util.List;

public interface AuthorityRepositoryCustom {
    List<Integer> findAuthorityIdsByNames(Collection<String> names);
}

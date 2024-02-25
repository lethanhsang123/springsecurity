package com.example.springsecurity.repository.custom;

import java.util.Collection;
import java.util.List;

public interface RoleRepositoryCustom {
    List<Integer> findRoleIdsByNames(Collection<String> names);
}

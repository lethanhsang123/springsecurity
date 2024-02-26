package com.example.springsecurity.repository.custom;

import com.example.springsecurity.entity.Role;

import java.util.Collection;
import java.util.List;

public interface RoleRepositoryCustom {
    List<Role> findAllByRole(Role role);
}

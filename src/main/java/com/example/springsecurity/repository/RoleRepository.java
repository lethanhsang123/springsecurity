package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Role;
import com.example.springsecurity.repository.custom.RoleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends RoleRepositoryCustom, JpaRepository<Role, Integer> {
    List<Role> findAllByName(Collection<String> names);
}

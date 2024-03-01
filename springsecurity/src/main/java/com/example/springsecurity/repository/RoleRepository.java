package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Authority;
import com.example.springsecurity.entity.Role;
import com.example.springsecurity.repository.custom.RoleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends RoleRepositoryCustom, JpaRepository<Role, Integer> {
    @Query(
            value = "SELECT r.id, r.name FROM role r JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = :uId",
            nativeQuery = true
    )
    List<Role> findAllByUserId(@Param("uId") Integer userId);
}

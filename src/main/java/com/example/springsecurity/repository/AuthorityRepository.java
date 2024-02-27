package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Authority;
import com.example.springsecurity.repository.custom.AuthorityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuthorityRepository extends AuthorityRepositoryCustom, JpaRepository<Authority, Integer> {

    @Query(
            value = "SELECT a.id, a.name FROM authority a JOIN user_authority au ON a.id = au.authority_id WHERE au.user_id = :uId",
            nativeQuery = true
    )
    List<Authority> findAllByUserId(@Param("uId") Integer userId);
}

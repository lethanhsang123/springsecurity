package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Token;
import com.example.springsecurity.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Integer> {
}

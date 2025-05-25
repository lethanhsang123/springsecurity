package com.vn.sanzee.auth_server.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "`user`")
public record User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,

        String username,

        String password,

        String phone,

        String email,

        @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_mtm_role",
                joinColumns = {
                        @JoinColumn(name = "user_id")
                },
                inverseJoinColumns = {
                        @JoinColumn(name = "role_id")
                }
        )
        List<Role> roleList
) {
}

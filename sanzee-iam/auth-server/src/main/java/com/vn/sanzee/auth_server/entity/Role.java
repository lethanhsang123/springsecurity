package com.vn.sanzee.auth_server.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "`role`")
public record Role(
        @Id
        @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
        Long id,

        String roleCode,
        @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
        @JoinTable(
                name = "role_mtm_permission",
                joinColumns = {
                        @JoinColumn(name = "role_id")
                },
                inverseJoinColumns = {
                        @JoinColumn(name = "permission_id")
                }
        )
        List<Permission> permissions
) {
}

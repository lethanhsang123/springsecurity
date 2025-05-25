package com.vn.sanzee.auth_server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "`permission`")
public record Permission(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,
        String permissionName,
        String permissionCode) {
}

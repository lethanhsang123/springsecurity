package com.vn.sanzee.auth_server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "`oauth2_client_role`")
public record OAuth2ClientRole(
        @Id
        Long id,

        String clientRegistrationId,

        String roleCode,

        @ManyToOne
        @JoinTable(
                name = "oauth2_client_role_mapping",
                joinColumns = {
                        @JoinColumn(name = "oauth_client_role_id")},
                inverseJoinColumns = {
                        @JoinColumn(name = "role_id")
                }
        )
        Role role
) {
}

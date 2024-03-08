package com.example.authorizationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Entity
@Setter
@Getter
@Table(name = "grant_types")
public class GrantType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "grant_type")
    private String grantType;

    @ManyToOne
    private Client client;

    public static GrantType from(AuthorizationGrantType authorizationGrantType, Client c) {
        GrantType g = new GrantType();
        g.setGrantType(authorizationGrantType.getValue());
        g.setClient(c);

        return g;
    }
}

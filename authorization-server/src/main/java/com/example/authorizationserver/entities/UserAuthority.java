package com.example.authorizationserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_authority")
@Table(schema = "user_authority")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer uId;

    @Column(name = "authority_id")
    private Integer authorityId;
}

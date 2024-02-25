package com.example.springsecurity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user_authrority")
@Table(schema = "user_authrority")
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

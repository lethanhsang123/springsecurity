package com.example.springsecurity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "token")
@Table(name = "token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "token")
    private String token;

}

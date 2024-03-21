package com.example.resourceserver.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "restaurant")
@Data
public class Restaurant {

    @Id
    @GeneratedValue
    public Long id;

    private String name;

    private String location;

    @Column(name = "type_name")
    private String type;

}

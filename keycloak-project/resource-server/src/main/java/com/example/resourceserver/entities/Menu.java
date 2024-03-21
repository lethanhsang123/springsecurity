package com.example.resourceserver.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "menu")
@Entity
@Data
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    private Long restaurantId;

    private Boolean active;

    @Transient
    private List<MenuItem> menuItems;

}

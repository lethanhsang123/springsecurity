package com.example.resourceserver.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Table(name = "menu_item")
@Entity
@Data
public class MenuItem {

    @Id
    @GeneratedValue
    public Long id;

    private Long menuId;

    private String name;

    private String description;

    @Column(name = "type_name")
    private String type;

    @Column(name = "group_name")
    private String group;

    private BigDecimal price;

}

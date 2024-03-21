package com.example.resourceserver.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Table(name = "order_item")
@Entity
@Data
public class OrderItem{

    @Id
    @GeneratedValue
    public Long id;

    private Long orderId;

    private Long menuItemId;

    private BigDecimal price;

}
package com.example.resourceserver.controllers;

import com.example.resourceserver.entities.Order;
import com.example.resourceserver.entities.OrderItem;
import com.example.resourceserver.repositories.OrderItemRepository;
import com.example.resourceserver.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    @GetMapping
    @RequestMapping("/{restaurantId}/list")
    // manager can access (suresh)
    public List<Order> getOrders(@PathVariable Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping
    @RequestMapping("/{orderId}")
    // manager can access (suresh)
    public Order getOrderDetails(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderItems(orderItemRepository.findByOrderId(order.getId()));
        return order;
    }

    @PostMapping
    // authenticated users can access
    public Order createOrder(Order order) {
        orderRepository.save(order);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> {
            orderItem.setOrderId(order.id);
            orderItemRepository.save(orderItem);
        });
        return order;
    }

    @GetMapping("/test")
    public String test1() {
        System.out.println("GET");
        return "GET test";
    }

    @PostMapping("/test")
    public String test2() {
        System.out.println("POST");
        return "POST test";
    }

}
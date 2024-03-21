package com.example.resourceserver.controllers;

import com.example.resourceserver.entities.Menu;
import com.example.resourceserver.entities.MenuItem;
import com.example.resourceserver.entities.Restaurant;
import com.example.resourceserver.repositories.MenuItemRepository;
import com.example.resourceserver.repositories.MenuRepository;
import com.example.resourceserver.repositories.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
//@SecurityRequirement(name = "Keycloak")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    private final MenuItemRepository menuItemRepository;

    @GetMapping
    @RequestMapping("/public/list")
    //Public API
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping
    @RequestMapping("/public/menu/{restaurantId}")
    //Public API
    public Menu getMenu(@PathVariable Long restaurantId) {
        Menu menu = menuRepository.findByRestaurantId(restaurantId);
        menu.setMenuItems(menuItemRepository.findAllByMenuId(menu.getId()));
        return menu;
    }

    // admin can access (admin)
//    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PostMapping
    @RequestMapping("/menu")
    // manager can access (suresh)
//    @PreAuthorize("hasRole('manager')")
    public Menu createMenu(Menu menu) {
        menuRepository.save(menu);
        menu.getMenuItems().forEach(menuItem -> {
            menuItem.setMenuId(menu.getId());
            menuItemRepository.save(menuItem);
        });
        return menu;
    }

    @PutMapping
    @RequestMapping("/menu/item/{itemId}/{price}")
    // owner can access (amar)
//    @PreAuthorize("hasRole('owner')")
    public MenuItem updateMenuItemPrice(@PathVariable Long itemId
            , @PathVariable BigDecimal price) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(itemId);
        menuItem.get().setPrice(price);
        menuItemRepository.save(menuItem.get());
        return menuItem.get();
    }


}

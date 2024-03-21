package com.example.resourceserver.repositories;

import com.example.resourceserver.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu findByRestaurantId(Long id);

}

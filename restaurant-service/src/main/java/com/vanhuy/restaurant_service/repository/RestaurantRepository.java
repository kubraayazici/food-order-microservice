package com.vanhuy.restaurant_service.repository;

import com.vanhuy.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}

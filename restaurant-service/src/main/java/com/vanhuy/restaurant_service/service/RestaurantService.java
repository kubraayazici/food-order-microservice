package com.vanhuy.restaurant_service.service;

import com.vanhuy.restaurant_service.dto.RestaurantDTO;
import com.vanhuy.restaurant_service.exception.RestaurantNotFoundException;
import com.vanhuy.restaurant_service.model.Restaurant;
import com.vanhuy.restaurant_service.repository.MenuItemRepository;
import com.vanhuy.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final ImageService imageService;

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant(
                restaurantDTO.restaurantId(),
                restaurantDTO.name(),
                restaurantDTO.address(),
                restaurantDTO.image()
        );
        restaurantRepository.save(restaurant);
        return toDTO(restaurant);
    }


    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // upload image for restaurant
    public RestaurantDTO uploadImage(Integer restaurantId, MultipartFile file) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        String fileName = imageService.uploadImage(file);

        restaurant.setImage(fileName);
        restaurantRepository.save(restaurant);
        return toDTO(restaurant);
    }

    @Cacheable(value = "restaurants" , key = "#pageable.pageNumber")
    public Page<RestaurantDTO> getRestaurantsByPage(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(this::toDTO);
    }
    private RestaurantDTO toDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getRestaurantId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getImage()
        );
    }

    @Cacheable(value = "restaurant", key = "#restaurantId")
    public Restaurant getRestaurantById(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
    }
}

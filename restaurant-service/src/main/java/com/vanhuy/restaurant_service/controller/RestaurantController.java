package com.vanhuy.restaurant_service.controller;


import com.vanhuy.restaurant_service.dto.RestaurantDTO;
import com.vanhuy.restaurant_service.exception.RestaurantNotFoundException;
import com.vanhuy.restaurant_service.service.ImageService;
import com.vanhuy.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO createdRestaurant = restaurantService.createRestaurant(restaurantDTO);
        return new ResponseEntity<>(createdRestaurant ,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantDTO>> getAllRestaurants(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(restaurantService.getRestaurantsByPage(pageable));
    }


    @PostMapping("/{restaurantId}/upload-image")
    public ResponseEntity<RestaurantDTO> uploadImage(
            @PathVariable Integer restaurantId,
            @RequestParam("file") MultipartFile file) {
        try {
            RestaurantDTO updatedRestaurant = restaurantService.uploadImage(restaurantId, file);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Resource resource = imageService.getImage(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

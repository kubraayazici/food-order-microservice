package com.vanhuy.user_service.controller;

import com.vanhuy.user_service.dto.ProfileResponse;
import com.vanhuy.user_service.dto.ProfileUpdateDTO;
import com.vanhuy.user_service.dto.UserDTO;
import com.vanhuy.user_service.model.User;
import com.vanhuy.user_service.service.FileStorageService;
import com.vanhuy.user_service.service.UserService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getProfile(user.getUserId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestPart("profile") ProfileUpdateDTO profileDTO,
            @RequestPart(value = "image", required = false) MultipartFile profileImage) {

        Optional.ofNullable(profileImage).ifPresent(this::validateImage);

        return ResponseEntity.ok(userService.updateProfile(user.getUserId(), profileDTO, profileImage));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/profile/image/{fileName:.+}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file to upload");
        }

        if (!Optional.ofNullable(file.getContentType()).filter(type -> type.startsWith("image/")).isPresent()) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

}

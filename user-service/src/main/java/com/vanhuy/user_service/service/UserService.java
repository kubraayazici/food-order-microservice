package com.vanhuy.user_service.service;

import com.vanhuy.user_service.dto.ProfileResponse;
import com.vanhuy.user_service.dto.ProfileUpdateDTO;
import com.vanhuy.user_service.dto.UserDTO;
import com.vanhuy.user_service.exception.UserNotFoundException;
import com.vanhuy.user_service.model.User;
import com.vanhuy.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.base-url}")
    private String baseUrl;

    public UserDTO getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return toUserDTO(user);
    }

//    @CacheEvict(value = "users", key = "#user.username") // Clear cache when user deleted
    @Transactional
    public void deleteUserById(Integer userId) {
       try {
           User user = userRepository.findByUserId(userId);
           if (user == null) {
               throw new UserNotFoundException("User not found");
           }
           userRepository.deleteByUserId(userId);
       }catch (Exception e){
           log.error("Error deleting user with id: {}", userId);
           throw new UserNotFoundException(e.getMessage());
       }
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();
    }

    public ProfileResponse getProfile(Integer userId) {
        return userRepository.findById(userId)
                .map(this::buildProfileResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private ProfileResponse buildProfileResponse(User user) {
        String imageUrl = Optional.ofNullable(user.getProfileImageName())
                .map(fileName -> baseUrl + "/api/v1/users/profile/image/" + fileName)
                .orElse(null);

        return ProfileResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .address(user.getAddress())
                .profileImageUrl(imageUrl)
                .build();
    }

    public ProfileResponse updateProfile(Integer userId, ProfileUpdateDTO profileDTO,
                                         MultipartFile profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getUsername().equals(profileDTO.getUsername()) &&
                userRepository.existsByUsername(profileDTO.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (!user.getEmail().equals(profileDTO.getEmail()) &&
                userRepository.existsByEmail(profileDTO.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        user.setUsername(profileDTO.getUsername());
        user.setEmail(profileDTO.getEmail());
        user.setAddress(profileDTO.getAddress());

        if (profileImage != null && !profileImage.isEmpty()) {
            Optional.ofNullable(user.getProfileImageName())
                    .ifPresent(fileStorageService::deleteFile);

            String fileName = fileStorageService.storeFile(profileImage);
            user.setProfileImageName(fileName);
        }

        return buildProfileResponse(userRepository.save(user));
    }
}

package com.vanhuy.user_service.service;

import com.vanhuy.user_service.dto.UserDTO;
import com.vanhuy.user_service.exception.UserNotFoundException;
import com.vanhuy.user_service.model.User;
import com.vanhuy.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

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
}

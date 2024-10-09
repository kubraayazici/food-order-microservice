package com.vanhuy.user_service.service;

import com.vanhuy.user_service.dto.UserDTO;
import com.vanhuy.user_service.exception.UserNotFoundException;
import com.vanhuy.user_service.model.User;
import com.vanhuy.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUserById(Integer userId) {
        User user = userRepository.findByUserId(userId);
        return toUserDTO(user);
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();
    }

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
}

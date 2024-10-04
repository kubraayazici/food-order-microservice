package com.vanhuy.user_service.service;

import com.vanhuy.user_service.dto.UserDTO;
import com.vanhuy.user_service.model.User;
import com.vanhuy.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
}

package com.vanhuy.user_service.controller;

import com.vanhuy.user_service.dto.UserDTO;
import com.vanhuy.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}

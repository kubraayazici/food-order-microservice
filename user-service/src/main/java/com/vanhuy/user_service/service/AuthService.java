package com.vanhuy.user_service.service;

import com.vanhuy.user_service.component.JwtUtil;
import com.vanhuy.user_service.dto.AuthResponse;
import com.vanhuy.user_service.dto.LoginRequest;
import com.vanhuy.user_service.dto.RegisterRequest;
import com.vanhuy.user_service.dto.RegisterResponse;
import com.vanhuy.user_service.exception.UserNotFoundException;
import com.vanhuy.user_service.model.User;
import com.vanhuy.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(LoginRequest loginRequest) throws BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            log.info("User {} logged in successfully", loginRequest.getUsername());
            return new AuthResponse(jwt);
        }else {
            throw new BadCredentialsException("Invalid username or password");
        }

    }

    public RegisterResponse register (RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserNotFoundException("Username is already taken");
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserNotFoundException("Email is already in use");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Collections.singleton("ROLE_USER"));

        log.info("User {} registered successfully", registerRequest);
        userRepository.save(user);

        return new RegisterResponse("User registered successfully");
    }

}

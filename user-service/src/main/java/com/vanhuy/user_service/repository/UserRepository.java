package com.vanhuy.user_service.repository;

import com.vanhuy.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByUserId(Integer userId);

    void deleteByUserId(Integer userId);
}

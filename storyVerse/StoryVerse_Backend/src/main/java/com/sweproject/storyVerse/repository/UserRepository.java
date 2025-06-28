package com.sweproject.storyVerse.repository;

import com.sweproject.storyVerse.entity.User;
import com.sweproject.storyVerse.security.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // User findUserByUserDetailsIdAndRole(Long userDetailsId, Role role);
    Optional<User> findByEmail(String email);
}

package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.Admin;
import com.sweproject.storyVerse.entity.Genre;
import com.sweproject.storyVerse.exception.AdminCreationException;
import com.sweproject.storyVerse.repository.AdminRepository;
import com.sweproject.storyVerse.security.auth.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    public void addAnAdmin(RegisterRequest request) {
        try {
            Admin admin = new Admin();
            admin.setGender(request.getGender());
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setFirstName(request.getFirstName());
            admin.setLastName(request.getLastName());
            admin.setDescription(request.getDescription());
            admin.setBirthDate(request.getBirthDate());
            admin.setGenres(null);
            admin.setRole(request.getRole());
            adminRepository.save(admin);
        }
        catch (DataIntegrityViolationException e) {
            // For DB constraint violations like duplicate email
            throw new AdminCreationException("Email already exists or violates database constraints.", e);
        }
        catch (Exception e){
            // Generic fallback
            throw new AdminCreationException("An unexpected error occurred while creating an admin.", e);
        }
    }
}

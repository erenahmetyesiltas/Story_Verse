package com.sweproject.storyVerse.controller;

import com.sweproject.storyVerse.security.auth.RegisterRequest;
import com.sweproject.storyVerse.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor
public class AdminController  {
    private final AdminService adminService;

    @PostMapping
    public void postAnAuthor(@RequestBody RegisterRequest request){
        adminService.addAnAdmin(request);
    }

    @GetMapping("/endpoint")
    public String getEnd(){
        return "Admin Endpoint";
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN') and #email == authentication.principal.email")
    public ResponseEntity<?> getAdminPage(@PathVariable String email) {
        return ResponseEntity.ok("Admin sayfası: " + email);
    }

}

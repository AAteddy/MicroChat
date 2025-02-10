
package com.tedsaasfaha.MicroChat.controller;


import com.tedsaasfaha.MicroChat.dto.PagedResponse;
import com.tedsaasfaha.MicroChat.dto.UserRegistrationDTO;
import com.tedsaasfaha.MicroChat.dto.UserResponseDTO;
import com.tedsaasfaha.MicroChat.model.Role;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(
            @RequestBody UserRegistrationDTO registrationDTO
            ) {

        if (userService.isExist(registrationDTO.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Admin with the email already exist.");
        }

        User adminUser = new User();
        adminUser.setName(registrationDTO.name());
        adminUser.setEmail(registrationDTO.email());
        adminUser.setPassword(registrationDTO.password());
        adminUser.setRole(Role.ADMIN); // Default admin role

        User savedAdmin = userService.registerUser(adminUser);
        return ResponseEntity.ok("Successfully registered Admin: " + savedAdmin);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            Pageable pageable
    ) {

        Page<UserResponseDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId
    ) {

        userService.removeUser(userId);
        return ResponseEntity.ok("Successfully removed user.");
    }

}
//
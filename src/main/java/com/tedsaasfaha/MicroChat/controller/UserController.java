package com.tedsaasfaha.MicroChat.controller;


import com.tedsaasfaha.MicroChat.dto.UserUpdateDTO;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @RequestBody UserUpdateDTO updateDTO,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        User updatedUser = userService.updateProfile(updateDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedUser);
    }
}


package com.tedsaasfaha.MicroChat.controller;


import com.tedsaasfaha.MicroChat.dto.PagedResponse;
import com.tedsaasfaha.MicroChat.dto.UserResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserUpdateDTO;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestBody UserUpdateDTO updateDTO,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        UserResponseDTO updatedUser = userService.updateProfile(updateDTO, userDetails.getUsername());
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long userId) {
        UserResponseDTO responseDTO = userService.getUserById(userId);
        responseDTO.addLinks();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedResponse<UserResponseDTO>> getAllActiveUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<UserResponseDTO> response = userService.getAllActiveUsers(pageable);
        return ResponseEntity.ok(response);
    }

}
//
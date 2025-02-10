
package com.tedsaasfaha.MicroChat.service;

import com.tedsaasfaha.MicroChat.dto.*;
import com.tedsaasfaha.MicroChat.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    boolean isExist(String email);

    User registerUser(User user);

    UserResponseDTO updateProfile(UserUpdateDTO updateDTO, String email);

    AuthResponseDTO createAuthenticationToken(AuthRequestDTO authRequestDTO) throws Exception;

    AuthResponseDTO createAuthRefreshToken(AuthResponseDTO responseDTO) throws Exception;

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    PagedResponse<UserResponseDTO> getAllActiveUsers(Pageable pageable);

    void removeUser(Long userId);

    void initiatePasswordReset(String email);

    void completePasswordReset(String token, String newPassword);

    UserResponseDTO getUserById(Long userId);
}
//
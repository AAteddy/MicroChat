
package com.tedsaasfaha.MicroChat.service;

import com.tedsaasfaha.MicroChat.dto.AuthRequestDTO;
import com.tedsaasfaha.MicroChat.dto.AuthResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserUpdateDTO;
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
}
//
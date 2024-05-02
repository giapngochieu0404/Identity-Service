package com.hieuubuntu.identityservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hieuubuntu.identityservice.constants.enums.Role;
import com.hieuubuntu.identityservice.constants.enums.UserStatus;
import com.hieuubuntu.identityservice.dto.request.user.UserCreateRequest;
import com.hieuubuntu.identityservice.dto.request.user.UserUpdateRequest;
import com.hieuubuntu.identityservice.dto.response.user.UserResponse;
import com.hieuubuntu.identityservice.entity.User;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.AppException;
import com.hieuubuntu.identityservice.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByUsername(userCreateRequest.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        }

        User newUser = new User();
        newUser.setUsername(userCreateRequest.getUsername());
        newUser.setFullname(userCreateRequest.getFullname());
        newUser.setStatusId(UserStatus.ACTIVE);
        newUser.setCreatedBy(UserStatus.ACTIVE.getValue());

        // Encrypt Password:
        newUser.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));

        return UserResponse.of(userRepository.save(newUser));
    }

    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }
        return UserResponse.of(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getStatusId() != null) {
            user.setStatusId(UserStatus.of(request.getStatusId()));
        }

        user.setModifiedBy(Role.ADMIN.getValue());

        userRepository.save(user);

        return UserResponse.of(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        // TODO: giải mã:
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }
        return UserResponse.of(user);
    }
}

package com.hieuubuntu.IdentityService.service;

import com.hieuubuntu.IdentityService.constants.enums.Role;
import com.hieuubuntu.IdentityService.constants.enums.UserStatus;
import com.hieuubuntu.IdentityService.dto.request.user.UserCreateRequest;
import com.hieuubuntu.IdentityService.dto.request.user.UserUpdateRequest;
import com.hieuubuntu.IdentityService.dto.response.user.UserResponse;
import com.hieuubuntu.IdentityService.entity.User;
import com.hieuubuntu.IdentityService.exception.error_code.ErrorCode;
import com.hieuubuntu.IdentityService.exception.type.AppException;
import com.hieuubuntu.IdentityService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByUsername(userCreateRequest.getUsername())) {
             throw new AppException(ErrorCode.USERNAME_EXISTS);
        }

        User newUser = new User();
        newUser.setUsername(userCreateRequest.getUsername());
        newUser.setPassword(userCreateRequest.getPassword());
        newUser.setFullname(userCreateRequest.getFullname());
        newUser.setStatusId(UserStatus.ACTIVE);
        newUser.setRole(Role.of(userCreateRequest.getRole()));
        newUser.setCreatedBy(UserStatus.ACTIVE.getValue());

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
            user.setPassword(request.getPassword());
        }

        if (request.getRole() != null) {
            user.setRole(Role.of(request.getRole()));
        }

        if (request.getStatusId() != null) {
            user.setStatusId(UserStatus.of(request.getStatusId()));
        }

        // Todo
        user.setModifiedBy(Role.ADMIN.getValue());

        userRepository.save(user);

        return UserResponse.of(user);
    }
}

package com.hieuubuntu.identityservice.controller;

import com.hieuubuntu.identityservice.dto.request.user.UserCreateRequest;
import com.hieuubuntu.identityservice.dto.request.user.UserUpdateRequest;
import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.dto.response.user.UserResponse;
import com.hieuubuntu.identityservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    DefaultResponse create(@RequestBody @Valid UserCreateRequest request) {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.createUser(request));
        return response;
    }

    @GetMapping
    DefaultResponse getByUserName(@RequestParam(name = "username") String username) {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.getByUsername(username));
        return response;
    }

    @PutMapping("/{user_id}")
    DefaultResponse updateUser(@PathVariable("user_id") Long userId, @RequestBody UserUpdateRequest request) {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.updateUser(userId, request));
        return response;
    }
}

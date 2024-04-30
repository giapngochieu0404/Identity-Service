package com.hieuubuntu.identityservice.controller;

import com.hieuubuntu.identityservice.dto.request.user.UserCreateRequest;
import com.hieuubuntu.identityservice.dto.request.user.UserUpdateRequest;
import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.dto.response.user.UserResponse;
import com.hieuubuntu.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    DefaultResponse<UserResponse> create(@RequestBody @Valid UserCreateRequest request) {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.createUser(request));
        return response;
    }

    @GetMapping
    DefaultResponse<UserResponse> getByUserName(@RequestParam(name = "username") String username) {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.getByUsername(username));
        return response;
    }

    @PutMapping("/{user_id}")
    DefaultResponse<UserResponse> updateUser(@PathVariable("user_id") Long userId, @RequestBody UserUpdateRequest request) {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.updateUser(userId, request));
        return response;
    }


    @GetMapping("/get-info")
    DefaultResponse<UserResponse> getMyInfo() {
        DefaultResponse<UserResponse> response = new DefaultResponse<>();
        response.setData(userService.getMyInfo());
        return response;
    }

}

package com.hieuubuntu.identityservice.clients.userprofile;

import com.hieuubuntu.identityservice.configuration.datasource.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hieuubuntu.identityservice.clients.ClientConfiguration;
import com.hieuubuntu.identityservice.dto.request.userprofile.CreateUserProfileRequest;
import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.dto.response.userprofile.UserProfileResponse;

@FeignClient(
        name = "user-profile-service",
        url = "${base-url.user-profile}",
        configuration = {ClientConfiguration.class, AuthenticationRequestInterceptor.class}
)
public interface UserProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DefaultResponse<UserProfileResponse>> createProfile(@RequestBody CreateUserProfileRequest request);

    @GetMapping(value = "/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DefaultResponse<UserProfileResponse>> getProfile(@RequestParam(name = "user_id") Integer userId);
}

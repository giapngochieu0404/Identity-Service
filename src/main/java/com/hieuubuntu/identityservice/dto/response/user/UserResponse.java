package com.hieuubuntu.identityservice.dto.response.user;

import java.time.LocalDateTime;

import com.hieuubuntu.identityservice.dto.response.userprofile.UserProfileResponse;
import com.hieuubuntu.identityservice.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String fullname;
    private String statusId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer modifiedBy;
    private UserProfileResponse detailProfile;

    public static UserResponse of(User user, UserProfileResponse profile) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .statusId(user.getStatusId().getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .modifiedBy(user.getModifiedBy())
                .detailProfile(profile)
                .build();
    }
}

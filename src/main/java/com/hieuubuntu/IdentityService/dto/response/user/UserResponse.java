package com.hieuubuntu.IdentityService.dto.response.user;

import com.hieuubuntu.IdentityService.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String statusId;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer modifiedBy;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullname(user.getFullname())
                .statusId(user.getStatusId().getName())
                .role(user.getRole().getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .modifiedBy(user.getModifiedBy())
                .build();
    }
}

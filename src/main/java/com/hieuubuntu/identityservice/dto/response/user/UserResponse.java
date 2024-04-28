package com.hieuubuntu.identityservice.dto.response.user;

import com.hieuubuntu.identityservice.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

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

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .statusId(user.getStatusId().getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .modifiedBy(user.getModifiedBy())
                .build();
    }
}

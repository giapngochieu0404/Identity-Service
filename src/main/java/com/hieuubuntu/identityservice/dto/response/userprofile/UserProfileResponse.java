package com.hieuubuntu.identityservice.dto.response.userprofile;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    @JsonProperty("user_profile_id")
    private Long userProfileId;

    @JsonProperty("user_id")
    private Integer userId;

    private LocalDate birthday;

    @JsonProperty("position_id")
    private Integer positionId;

    private String address;
}

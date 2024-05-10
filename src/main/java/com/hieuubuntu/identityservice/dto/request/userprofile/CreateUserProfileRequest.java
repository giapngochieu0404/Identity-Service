package com.hieuubuntu.identityservice.dto.request.userprofile;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserProfileRequest {
    @NotNull(message = "Thiếu userId")
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull(message = "Thiếu ngày sinh")
    private LocalDate birthday;

    @JsonProperty("position_id")
    @NotNull(message = "Thiếu vị trí")
    private Integer positionId;

    @NotNull(message = "Thiếu địa chỉ")
    private String address;
}

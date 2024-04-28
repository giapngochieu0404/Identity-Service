package com.hieuubuntu.identityservice.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String password;
    private String fullname;

    @JsonProperty("status_id")
    private Integer statusId;
    private Integer role;
}

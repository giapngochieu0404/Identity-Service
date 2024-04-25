package com.hieuubuntu.IdentityService.dto.response.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    private boolean isValid;
}

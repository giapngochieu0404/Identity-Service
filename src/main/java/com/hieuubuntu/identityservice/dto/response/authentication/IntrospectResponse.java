package com.hieuubuntu.identityservice.dto.response.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    private boolean isValid;
}

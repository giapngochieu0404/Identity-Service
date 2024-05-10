package com.hieuubuntu.identityservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        String body = response.body().toString();
        log.error(
                "Lỗi khi call API {}, method {}, HttpStatus {} - Lý do {}",
                response.request().url(),
                s,
                response.status(),
                body);
        return this.errorDecoder.decode(s, response);
    }
}

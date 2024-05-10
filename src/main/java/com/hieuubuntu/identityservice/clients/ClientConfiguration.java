package com.hieuubuntu.identityservice.clients;

import org.springframework.context.annotation.Bean;

import com.hieuubuntu.identityservice.exception.CustomErrorDecoder;

import feign.codec.ErrorDecoder;

public class ClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}

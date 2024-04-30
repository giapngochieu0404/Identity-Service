package com.hieuubuntu.identityservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] publicEndpoints = {"/auth/login", "/auth/introspect", "/auth/logout", "/auth/introspect", "/auth/refresh-token"};

    private final JwtDecoderImpl jwtDecoderImpl;

    public SecurityConfig(JwtDecoderImpl jwtDecoderImpl) {
        this.jwtDecoderImpl = jwtDecoderImpl;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, publicEndpoints).permitAll()
                .anyRequest().authenticated());

        // Đăng kí 1 provider manager support cho Authentication provider
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoderImpl)) // Cung cấp cho authentication provider 1 decoder để decode token
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // Bắt lỗi 401: Do lỗi 401 xảy ra ở tầng filter, chưa vào đến tầng application nên phải bắt ở đây.
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

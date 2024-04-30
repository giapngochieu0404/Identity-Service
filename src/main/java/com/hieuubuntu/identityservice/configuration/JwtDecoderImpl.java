package com.hieuubuntu.identityservice.configuration;

import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.AppException;
import com.hieuubuntu.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;


// Cung cấp cho authentication provider 1 decoder:
// Các thuật toán + thông số như cách đã mã hóa và tạo ra token trước đó

@Component
public class JwtDecoderImpl implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String singerKey;

    private final AuthenticationService authenticationService;

    public JwtDecoderImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // JwtDecoder được sử dụng trong vòng lặp forEach, khởi tạo Nimbus 1 lần duy nhất.
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            var isValidToken = authenticationService.verifyToken(token);
            if (isValidToken == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(singerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);

    }
}

package com.hieuubuntu.identityservice.configuration;

import java.text.ParseException;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.hieuubuntu.identityservice.service.AuthenticationService;

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
        // Setup service không thể call trực tiếp từ bên ngoài mà phải call thông qua gateway
        // Việc xác thực sẽ là không cần thiết nữa thì gateway đã call sang để introspect rồi
        // nên comment đoạn này lại

//        try {
//            var isValidToken = authenticationService.verifyToken(token, false);
//            if (isValidToken == null) {
//                throw new AppException(ErrorCode.UNAUTHENTICATED);
//            }
//        } catch (JOSEException | ParseException e) {
//            throw new JwtException(e.getMessage());
//        }

//        if (Objects.isNull(nimbusJwtDecoder)) {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(singerKey.getBytes(), "HS512");
//            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                    .macAlgorithm(MacAlgorithm.HS512)
//                    .build();
//        }
//
//        return nimbusJwtDecoder.decode(token);

        // token truyền vào bây giờ đã là token đúng:

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}

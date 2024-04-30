package com.hieuubuntu.identityservice.service;

import com.hieuubuntu.identityservice.dto.request.authentication.IntrospectRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.LoginRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.LogoutRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.RefreshTokenRequest;
import com.hieuubuntu.identityservice.dto.response.authentication.AuthenticationResponse;
import com.hieuubuntu.identityservice.dto.response.authentication.IntrospectResponse;
import com.hieuubuntu.identityservice.dto.response.user.UserResponse;
import com.hieuubuntu.identityservice.entity.InvalidToken;
import com.hieuubuntu.identityservice.entity.User;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.AppException;
import com.hieuubuntu.identityservice.repositories.InvalidTokenRepository;
import com.hieuubuntu.identityservice.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final InvalidTokenRepository invalidTokenRepository;

    public AuthenticationService(
            UserRepository userRepository,
            InvalidTokenRepository invalidTokenRepository) {
        this.userRepository = userRepository;
        this.invalidTokenRepository = invalidTokenRepository;
    }

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    public AuthenticationResponse login(LoginRequest request) throws JOSEException {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new AppException(ErrorCode.USER_PASSWORD_INCORRECT);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.USER_PASSWORD_INCORRECT);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    // verify token: check token có phải được tạo ra từ hệ thống vs thuật toán + secret key của hệ thống hay không
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // Verify token có hợp lệ hay không: Không hợp lệ sẽ throw Unauthenticated
        var isValid = verifyToken(request.getToken());

        return IntrospectResponse.builder()
                .isValid(isValid != null)
                .build();
    }


    // Logout:
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var token = request.getToken();

        // Verify token có hợp lệ hay không: Không hợp lệ sẽ throw Access Denied Exception
        SignedJWT signedJWT = verifyToken(token);

        if (signedJWT == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        saveIsValidToken(signedJWT, token);

    }


    private void saveIsValidToken(SignedJWT signedJWT, String token) throws ParseException {
        // Get jwt Id:
        var jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        // Lưu vào bảng invalid_tokens:
        InvalidToken invalidToken = InvalidToken.builder()
                .id(jwtId)
                .token(token)
                .expiredAt(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

        invalidTokenRepository.save(invalidToken);
    }


    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // check time expired:
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        var verified = signedJWT.verify(verifier);

        if (!verified
                || !expireTime.after(new Date()) // Đã hết hạn
                || invalidTokenRepository.existsById(jwtId) // Đã bị logout
        ) {
            return null;
        }

        return signedJWT;
    }

    private String generateToken(User user) throws JOSEException {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // payload:
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // mã hóa
                .issuer("hieuubuntu.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("clientId", "hieuubuntu")
                .jwtID(UUID.randomUUID().toString()) // Dùng để logout
                .build();


        Payload jwtPayload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, jwtPayload);

        //Sign
        jwsObject.sign(new MACSigner(signerKey.getBytes()));

        return jwsObject.serialize();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws JOSEException, ParseException {
        // verify token:
        SignedJWT signedJWT = verifyToken(request.getToken());
        if (signedJWT == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Huỷ token cũ:
        saveIsValidToken(signedJWT, request.getToken());

        String username = signedJWT.getJWTClaimsSet().getSubject(); // Giải mã
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        // Gen new token
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
}

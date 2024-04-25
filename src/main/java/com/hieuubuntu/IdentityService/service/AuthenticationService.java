package com.hieuubuntu.IdentityService.service;

import com.hieuubuntu.IdentityService.constants.enums.Role;
import com.hieuubuntu.IdentityService.constants.enums.UserStatus;
import com.hieuubuntu.IdentityService.dto.request.authentication.IntrospectRequest;
import com.hieuubuntu.IdentityService.dto.request.authentication.LoginRequest;
import com.hieuubuntu.IdentityService.dto.response.authentication.AuthenticationResponse;
import com.hieuubuntu.IdentityService.dto.response.authentication.IntrospectResponse;
import com.hieuubuntu.IdentityService.dto.response.user.UserResponse;
import com.hieuubuntu.IdentityService.entity.User;
import com.hieuubuntu.IdentityService.exception.error_code.ErrorCode;
import com.hieuubuntu.IdentityService.exception.type.AppException;
import com.hieuubuntu.IdentityService.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;

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

        var token = generateToken(user.getUsername());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    // verify token
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SINGER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // check time expired:
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .isValid(verified && expireTime.after(new Date()))
                .build();
    }

    private String generateToken(String username) throws JOSEException {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // payload:
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("hieuubuntu.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("clientId", "hieuubuntu")
                .build();


        Payload jwtPayload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, jwtPayload);

        //Sign
        jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));

        return jwsObject.serialize();
    }
}

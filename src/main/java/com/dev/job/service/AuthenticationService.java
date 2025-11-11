package com.dev.job.service;

import com.dev.job.dto.request.AuthenticationRequest;
import com.dev.job.dto.request.User.CreateUserRequest;
import com.dev.job.dto.response.AuthenticationResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.user.*;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ConflictException;
import com.dev.job.exceptions.UnauthenticatedException;
import com.dev.job.mapper.UserMapper;
import com.dev.job.repository.User.JobSeekerRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationService {
    UserRepository userRepository;
    RecruiterRepository recruiterRepository;
    JobSeekerRepository jobSeekerRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.access-key}")
    private String ACCESS_KEY;

    @NonFinal
    @Value("${jwt.refresh-key}")
    private String REFRESH_KEY;

    public UserResponse signup(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("Email already existed.");
        }

        if("JOBSEEKER".equals(request.getRole())){
            JobSeeker user = userMapper.createRequestToJobSeeker(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.JOBSEEKER);
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());
            return userMapper.userToResponse(jobSeekerRepository.save(user));
        } else if("RECRUITER".equals(request.getRole())){
            Recruiter user = userMapper.createRequestToRecruiter(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setStatus(UserStatus.ACTIVE);
            user.setRole(UserRole.RECRUITER);
            user.setCreatedAt(LocalDateTime.now());
            log.info(user.getUsername());
            log.info(request.getUsername());
            log.info(user.getPassword());
            log.info(request.getPassword());
            return userMapper.userToResponse(recruiterRepository.save(user));
        } else {
            throw new BadRequestException("Invalid role.");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        User user = userRepository.findByEmailOrUsername(request.getEmail(), request.getEmail()).orElseThrow(() ->
            new BadRequestException("Invalid email."));
        boolean passwordCorrect = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!passwordCorrect)
            throw new BadRequestException("Invalid password.");
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateAccessToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet
                .Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("do.dev")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("role", user.getRole())
                .claim("username", user.getUsername())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(ACCESS_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("Cannot create access token", e);
            throw new RuntimeException(e);
        }
    }

    public String refresh(String refreshToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            boolean verified = signedJWT.verify(new MACVerifier(REFRESH_KEY.getBytes()));
            if (!verified) throw new UnauthenticatedException("Invalid refresh token.");

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (!"refresh".equals(claims.getClaim("type"))) {
                throw new UnauthenticatedException("Invalid token type.");
            }

            UUID userId = UUID.fromString(claims.getSubject());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("User not found."));

            return  generateAccessToken(user);

        } catch (Exception e) {
            throw new UnauthenticatedException("Refresh token invalid or expired.");
        }
    }

    public boolean introspect(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verified = signedJWT.verify(new MACVerifier(ACCESS_KEY.getBytes()));
            if (!verified) return false;

            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    private String generateRefreshToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet
                .Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("do.dev")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("type", "refresh")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(REFRESH_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create refresh token", e);
            throw new RuntimeException(e);
        }
    }

}

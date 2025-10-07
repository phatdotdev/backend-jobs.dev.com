package com.dev.job.service;

import com.dev.job.dto.request.AuthenticationRequest;
import com.dev.job.dto.request.User.CreateUserRequest;
import com.dev.job.dto.response.AuthenticationResponse;
import com.dev.job.dto.response.UserResponse;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.entity.user.User;
import com.dev.job.entity.user.UserRole;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ConflictException;
import com.dev.job.mapper.UserMapper;
import com.dev.job.repository.User.JobSeekerRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;

    public UserResponse signup(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("Email already existed.");
        }

        if("JOBSEEKER".equals(request.getRole())){
            JobSeeker user = userMapper.createRequestToJobSeeker(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.JOBSEEKER);
            user.setCreatedAt(LocalDate.now());
            return userMapper.userToResponse(jobSeekerRepository.save(user));
        } else if("RECRUITER".equals(request.getRole())){
            Recruiter user = userMapper.createRequestToRecruiter(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.RECRUITER);
            user.setCreatedAt(LocalDate.now());
            return userMapper.userToResponse(recruiterRepository.save(user));
        } else {
            throw new BadRequestException("Invalid role.");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
            new BadRequestException("Invalid email or password."));
        boolean passwordCorrect = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!passwordCorrect)
            throw new BadRequestException("Invalid email or password.");
        String token = generateToken(user);
        return AuthenticationResponse.builder().authenticated(true).token(token).build();
    }

    private String generateToken(User user){
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
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}

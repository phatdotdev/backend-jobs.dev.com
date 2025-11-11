package com.dev.job.configuration;

import com.dev.job.exceptions.UnauthenticatedException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.access-key}")
    private String accessKey;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if(!signedJWT.verify(new MACVerifier(accessKey.getBytes()))){
                throw new UnauthenticatedException("Invalid JWT signature.");
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if(expirationTime.before(new Date())){
                throw new UnauthenticatedException("Token has expired.");
            }

            if(nimbusJwtDecoder == null){
                SecretKeySpec secretKeySpec = new SecretKeySpec(accessKey.getBytes(), "HmacSHA512");
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build();
            }

            return nimbusJwtDecoder.decode(token);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
//@Override
//public Jwt decode(String token) throws JwtException {
//    try {
//        SignedJWT signedJWT = SignedJWT.parse(token);
//
//        if (!signedJWT.verify(new MACVerifier(accessKey.getBytes()))) {
//            throw new UnauthenticatedException("Invalid JWT signature.");
//        }
//
//        if (nimbusJwtDecoder == null) {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(accessKey.getBytes(), "HmacSHA512");
//            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                    .macAlgorithm(MacAlgorithm.HS512)
//                    .build();
//        }
//
//        return nimbusJwtDecoder.decode(token);
//
//    } catch (JwtException ex) {
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(token);
//            return new Jwt(
//                    token,
//                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
//                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
//                    signedJWT.getHeader().toJSONObject(),
//                    signedJWT.getJWTClaimsSet().getClaims()
//            );
//        } catch (Exception inner) {
//            throw new JwtException("Token expired and cannot be parsed", inner);
//        }
//    } catch (JOSEException | ParseException e) {
//        throw new JwtException("Invalid token", e);
//    }
//}

}

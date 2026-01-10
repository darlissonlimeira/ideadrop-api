package io.github.darlissonlimeira.ideadrop.api.service;

import io.github.darlissonlimeira.ideadrop.api.exception.InvalidBearerTokenException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class JwtService {

    @Value("${app.jwt-config.secret-key}")
    private String secret;

    @Value("${app.jwt-config.iss}")
    private String issuer;

    @Value("${app.jwt-config.aud}")
    private String audience;

    public String createAccessToken(String userId) throws JoseException {
        var claims = getClaims(userId);
        claims.setExpirationTimeMinutesInTheFuture(1);

        return getJwt(claims);
    }

    public String createRefreshToken(String userId) throws JoseException {
        var exp = Instant.now().plus(30, ChronoUnit.DAYS).getEpochSecond();
        var claims = getClaims(userId);
        claims.setExpirationTime(NumericDate.fromSeconds(exp));

        return getJwt(claims);
    }

    public String verifyToken(@NotNull String token) throws InvalidBearerTokenException {
        var key = new HmacKey(secret.getBytes());
        var jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setRequireSubject()
                .setExpectedIssuer(issuer)
                .setExpectedAudience(audience)
                .setVerificationKey(key)
                .setJwsAlgorithmConstraints(ConstraintType.PERMIT, AlgorithmIdentifiers.HMAC_SHA256)
                .build();

        try {

            var jwt = jwtConsumer.process(token);
            var claims = jwt.getJwtClaims();

            return claims.getSubject();

        } catch (MalformedClaimException | InvalidJwtException e) {
            log.warn(e.getMessage());
            throw new InvalidBearerTokenException("Invalid Or Expired Token.");
        }
    }

    private String getJwt(JwtClaims claims) throws JoseException {
        var key = new HmacKey(secret.getBytes());
        var jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(key);

        return jws.getCompactSerialization();
    }

    private JwtClaims getClaims(String userId) {
        var claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setSubject(userId);
        claims.setAudience(audience);

        return claims;
    }
}

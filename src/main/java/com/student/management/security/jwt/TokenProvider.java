package com.student.management.security.jwt;

import io.jsonwebtoken.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jwtToken.timeout:5}")
    private int jwtTokenTimeout;

    @Value("${jwt.refresh.timeout:10}")
    private int jwtRefreshTokenTimeout;

    @Value("${jwt.secret:Q+fq8C0YcEr9We0R3Hk1TE5pYIuTnH7eD/LtI9T1roY=}")
    private String secret;

    public String createJwtToken(Authentication authentication) {
        return createToken(authentication, jwtTokenTimeout);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, jwtRefreshTokenTimeout);
    }

    public String createToken(Authentication authentication, int timeout) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (timeout * 60 * 1000)))
                .compressWith(CompressionCodecs.GZIP)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getBody();

        User principal = new User(claims.getSubject(), "", Collections.emptyList());

        return new UsernamePasswordAuthenticationToken(principal, token, Collections.emptyList());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).build().parseSignedClaims(authToken);

            return true;
        } catch (Exception e) {
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }
}

package com.student.management.controller;

import com.student.management.security.jwt.JWTFilter;
import com.student.management.security.jwt.TokenProvider;


import com.student.management.service.dto.LoginRequest;
import com.student.management.service.dto.LoginResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getLoginResponseResponseEntity(authentication);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody String token) {
        if(!tokenProvider.validateToken(token)) {
            throw new RuntimeException("SESSION_EXPIRED");
        }
        return getLoginResponseResponseEntity(tokenProvider.getAuthentication(token));
    }

    private ResponseEntity<LoginResponse> getLoginResponseResponseEntity(Authentication authentication) {
        String jwt = tokenProvider.createJwtToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new LoginResponse(jwt, refreshToken), httpHeaders, HttpStatus.OK);
    }

}

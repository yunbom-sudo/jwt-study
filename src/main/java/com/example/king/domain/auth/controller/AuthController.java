package com.example.king.domain.auth.controller;

import com.example.king.domain.auth.dto.request.LoginRequest;
import com.example.king.domain.auth.dto.response.TokenResponse;
import com.example.king.domain.auth.service.AuthService;
import com.example.king.global.cookie.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        TokenResponse tokenResponse = authService.login(request);

        ResponseCookie cookie = CookieUtil.createRefreshTokenCookie(
                tokenResponse.getRefreshToken()
        );

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return ResponseEntity.ok(
                TokenResponse.builder()
                        .accessToken(tokenResponse.getAccessToken())
                        .build()
        );
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
            @CookieValue("refreshToken") String refreshToken
    ) {

        TokenResponse response =
                authService.reissue(refreshToken);

        return ResponseEntity.ok(response);
    }

}

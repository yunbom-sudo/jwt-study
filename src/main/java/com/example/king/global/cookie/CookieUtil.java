package com.example.king.global.cookie;


import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    private CookieUtil() {
    }

    public static ResponseCookie createRefreshTokenCookie(
            String refreshToken
    ) {
        return ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // 개발 환경
                .sameSite("Strict")
                .path("/auth/reissue")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}

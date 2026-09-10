package com.example.king.domain.auth.service;

import com.example.king.domain.auth.dto.request.LoginRequest;
import com.example.king.domain.auth.dto.response.TokenResponse;
import com.example.king.domain.auth.user.entity.User;
import com.example.king.domain.auth.user.repository.UserRepository;
import com.example.king.global.exception.BusinessException;
import com.example.king.global.exception.ErrorCode;
import com.example.king.global.jwt.JwtProvider;
import com.example.king.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Transactional
    public TokenResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        redisService.saveRefreshToken(
                user.getId(),
                refreshToken,
                Duration.ofDays(7)
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public TokenResponse reissue(String refreshToken){

        if(!jwtProvider.validateToken(refreshToken)){
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        String savedRefreshToken = redisService.getRefreshToken(userId);

        if(savedRefreshToken==null || !savedRefreshToken.equals(refreshToken)){
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtProvider.createAccessToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}

package com.ssg.itbody.service;


import com.ssg.itbody.config.jwt.TokenProvider;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;


@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceImpl userServiceimpl;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7); // 이틀(리프레시 토큰의 유효기간)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 시간(억세스 토큰의 유효기간)

    public String createNewAccessToken(String refreshToken) {
        // !!!! 리프레시 토큰도 JWT스펙으로 만들어지기 때문에, TokenProvider에 의한 유효성 검증이 가능합니다. !!!!
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token"); // 재로그인 요청 메시지 전달
        }// 유효하지 않은 토큰이면 예외 발생후 종료
        // 리프레시토큰이 유효하지 않다면, 다시 발급해줘야하지만 로그인 로직에 발급 기능이 있고, 재로그인을 요구한다면 필수는 아니다.

        Long userId = refreshTokenRepository.findUserIdByRefreshToken(refreshToken);

        UserEntity userEntity = userServiceimpl.findById(userId);




    return tokenProvider.generateRefreshToken(userEntity,Duration.ofHours(2));
    }
}

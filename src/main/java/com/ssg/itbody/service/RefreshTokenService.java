//package com.ssg.itbody.service;
//
//
//import com.ssg.itbody.entity.RefreshToken;
//import com.ssg.itbody.repository.RefreshTokenRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class RefreshTokenService {
//
//    // 서비스 레이어는 레포지토리 레이어를 호출합니다.
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    public RefreshToken findByRefreshToken(String refreshToken) {
//        return refreshTokenRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(()->new IllegalArgumentException("Unexpected token"));
//    }
//
//
//
//}

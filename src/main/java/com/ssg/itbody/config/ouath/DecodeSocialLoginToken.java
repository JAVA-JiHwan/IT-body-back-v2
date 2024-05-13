package com.ssg.itbody.config.ouath;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ssg.itbody.dto.LoginResponseDTO;
import com.ssg.itbody.entity.Authority;
import com.ssg.itbody.entity.RefreshToken;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.config.jwt.TokenProvider;
import com.ssg.itbody.exception.BusinessException;
import com.ssg.itbody.exception.ExceptionCode;
import com.ssg.itbody.repository.RefreshTokenRepository;
import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DecodeSocialLoginToken {

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;


    private final RefreshTokenRepository refreshTokenRepository;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7); // 이틀(리프레시 토큰의 유효기간)
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2); // 시간(억세스 토큰의 유효기간)

    @Value("${jwt.client_id}")
    private String CLIENT_ID;


    public LoginResponseDTO decodingToken(String token) throws IOException, GeneralSecurityException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        GoogleIdToken idToken = verifier.verify(token);



        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String name = (String) payload.get("name");  // name 정보 가져오기

//            System.out.println("디코딩된 토큰" + idToken.getPayload());

            String email = payload.getEmail();

//            System.out.println("얻은 이메일" + email);

            UserEntity userEntity = userRepository.findByEmail(email) // JPA 는 기본 Optional객체 반환
                    .orElseGet(() -> { //없는 유저라면 null값이므로 새로 저장 로직. (람다식)
                        UserEntity newUser = UserEntity.builder()
                                .email(email)
                                .nickname(name)
                                .authority(Authority.USER)
                                .build();
                        newUser = userRepository.save(newUser);
                        return newUser; // if문 리턴이 아닌, user객체에 newUser 할당.
                    });

            // 엑세스 토큰 생성
            String accessToken = tokenProvider.generateAccessToken(userEntity, ACCESS_TOKEN_DURATION);
            // 리프레시토큰 생성 및 저장
            String refreshToken = tokenProvider.generateRefreshToken(userEntity, REFRESH_TOKEN_DURATION);



            RefreshToken newRefreshToken = new RefreshToken(userEntity.getUserId(), refreshToken);

            // DB에서 userId에 해당하는 refresh토큰 검색
            Optional<RefreshToken> existingToken = Optional.ofNullable(refreshTokenRepository.findByUserId(userEntity.getUserId()));

            // 기존 토근이 있든 없든 갱신하거나 새로 저장합니다.
            refreshTokenRepository.save(existingToken.orElse(newRefreshToken).update(newRefreshToken.getRefreshToken()));





            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .authority(String.valueOf(userEntity.getAuthority()))
                    .userId(userEntity.getUserId())
                    .email(userEntity.getEmail())
                    .nickname(userEntity.getNickname())
                    .build();


        } else {
            System.out.println("Invalid ID token.");
            throw new BusinessException(ExceptionCode.TOKEN_NOT_VALID);
        }




    }
}

package com.ssg.itbody.dto;

import com.ssg.itbody.entity.UserEntity;
import lombok.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String refreshToken;
    private String accessToken;
    private Long userId;
    private String email;
    private String nickname;
    private String authority;

    private String imageUrl;// 이미지 파일을 나타내는 필드

    public LoginResponseDTO(UserEntity userEntity){
        this.userId = userEntity.getUserId();
        this.email = userEntity.getEmail();
        this.nickname = userEntity.getNickname();
        this.authority = String.valueOf(userEntity.getAuthority());
        this.imageUrl = userEntity.getImageUrl();
    }

    public LoginResponseDTO(UserEntity userEntity, String refreshToken, String accessToken) {
    }


}

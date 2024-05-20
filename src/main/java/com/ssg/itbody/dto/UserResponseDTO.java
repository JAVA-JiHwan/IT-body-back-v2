package com.ssg.itbody.dto;


import com.ssg.itbody.entity.UserEntity;
import lombok.*;



@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String nickname;
    private String imageUrl;
    private String email;
    private String authority;


    public static UserResponseDTO from(UserEntity userEntity) {
        return UserResponseDTO.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .authority(userEntity.getAuthority().name())
                .imageUrl(userEntity.getImageUrl())
                .build();
    }

}

package com.ssg.itbody.dto;


import com.ssg.itbody.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class UserResponseDTO {
    private Long userId;
    private String nickname;
    private String imageUrl;


    public static UserResponseDTO from(UserEntity userEntity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(userEntity.getUserId());
        dto.setNickname(userEntity.getNickname());
        dto.setImageUrl(userEntity.getImageUrl());


        return dto;
    }

}

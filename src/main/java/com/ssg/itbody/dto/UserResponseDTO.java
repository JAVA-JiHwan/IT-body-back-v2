package com.ssg.itbody.dto;

import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.enums.MembershipGrade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private Long userId;
    private String nickname;
    private String email;
    private String phone;
    private String gender;
    private String healthStatus;
    private String profileImgName;
    private String profileImgPath;
    private MembershipGrade membershipGrade;

    public static UserResponseDTO from(UserEntity user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .healthStatus(user.getHealthStatus())
                .profileImgName(user.getProfileImgName())
                .profileImgPath(user.getProfileImgPath())
                .membershipGrade(user.getMembershipGrade())
                .build();
    }
}

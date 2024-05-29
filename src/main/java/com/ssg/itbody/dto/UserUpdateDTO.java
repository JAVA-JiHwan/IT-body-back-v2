package com.ssg.itbody.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private Long userId;
    private String nickname;
    private String email;
    private String phone;
    private String healthStatus;
    private String profileImgName;
    private String profileImgPath;
}

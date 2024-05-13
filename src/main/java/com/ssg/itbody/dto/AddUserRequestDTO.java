package com.ssg.itbody.dto;


import lombok.Getter;
import lombok.Setter;
import com.ssg.itbody.entity.Authority;

@Getter
@Setter
public class AddUserRequestDTO {
    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private String confirmPassword;
    private Authority Authority;
}

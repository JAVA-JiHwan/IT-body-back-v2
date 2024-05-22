package com.ssg.itbody.dto;

import com.ssg.itbody.enums.MembershipGrade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserDTO {
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "잘못된 닉네임 형식입니다.")
    @Size(min = 3, max = 10, message = "닉네임은 최소 3글자, 최대 10글자까지 설정해주세요.")
    private String nickname;
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*./]*$", message = "잘못된 비밀번호 형식입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이여야 합니다.")
    private String password;
    
    private String phone;
    private String gender;
    private String healthStatus;
    private String imageUrl;
    private MembershipGrade membershipGrade;

}

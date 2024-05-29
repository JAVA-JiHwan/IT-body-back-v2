package com.ssg.itbody.entity;

import com.ssg.itbody.enums.MembershipGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "members")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long userId;

    @Column(name = "name")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "잘못된 닉네임 형식입니다.")
    @Size(min = 3, max = 10, message = "닉네임은 최소 3글자, 최대 10글자까지 설정해주세요.")
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Column(name = "password")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*./]*$", message = "잘못된 비밀번호 형식입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이여야 합니다.")
    private String password;

    @Column(name = "selphone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "health_status")
    private String healthStatus;

    @Column(name = "profileImgName")
    private String profileImgName;
    @Column(name = "profileImgPath")
    private String profileImgPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_grade")
    private MembershipGrade membershipGrade;

    @Builder
    public UserEntity(Long userId, String nickname, String email, String password, String phone, String gender, String healthStatus, String profileImgName, String profileImgPath, MembershipGrade membershipGrade) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.healthStatus = healthStatus;
        this.profileImgName = profileImgName;
        this.profileImgPath = profileImgPath;
        this.membershipGrade = membershipGrade;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + membershipGrade.getMembershipGrade())); // 사용자의 역할을 가져와서 "ROLE_"을 붙여서 반환
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public void setImage(MultipartFile file) throws IOException {
//        this.imageUrl = file.getBytes();
//    }

}
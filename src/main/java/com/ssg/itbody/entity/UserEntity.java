package com.ssg.itbody.entity;

import com.ssg.itbody.enums.Authority;
import com.ssg.itbody.enums.MembershipGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(name = "photo")
    private String imageUrl;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_grade")
    private MembershipGrade membershipGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Builder
    public UserEntity(String nickname, String email, String password, String phone, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, MembershipGrade membershipGrade, Authority authority) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.membershipGrade = membershipGrade;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return email;
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

    public static UserEntity createUser() {
        return new UserEntity();
    }


}
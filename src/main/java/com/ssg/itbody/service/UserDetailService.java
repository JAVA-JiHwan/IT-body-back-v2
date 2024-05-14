package com.ssg.itbody.service;

import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.exception.BusinessException;
import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserEntity loadUserByUsername(String email) {
        return userRepository.findByEmail(email) // 로그인 아이디 입력되면 회원정보 리턴
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}

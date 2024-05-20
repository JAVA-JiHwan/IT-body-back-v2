package com.ssg.itbody.service;

import com.ssg.itbody.entity.UserEntity;

import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {


    private final UserRepository userRepository;
    @Override
    public UserEntity loadUserByUsername(String email) {
        return userRepository.findByEmail(email) // 로그인 아이디 입력되면 회원정보 리턴
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일에 해당하는 사용자를 찾을 수 없습니다: " + email));
    }


//    @Override
//    public UserEntity loadUserByUsername(String email) {
//        return userRepository.findByEmail(email) // 로그인 아이디 입력되면 회원정보 리턴
//                .orElseThrow(() -> new IllegalArgumentException((email)));
//    }
}

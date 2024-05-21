package com.ssg.itbody.service;


import com.ssg.itbody.dto.CustomUserDetails;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String email) {
//        UserEntity userEntity = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//        return userEntity;
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByNickname(username);

        if (userData != null) {

            return new CustomUserDetails(userData);
        }

        return null;
    }
}
package com.ssg.itbody.service;


import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return userEntity;
    }
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<UserEntity> userData = userRepository.findByEmail(email);
//E
//        UserEntity userEntity = userData.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//
//        return new CustomuserDetails(userEntity);
//    }
}
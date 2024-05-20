package com.ssg.itbody.service;

import com.ssg.itbody.dto.UserDTO;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.exception.DuplicateUserException;
import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserDTO userDTO) {

        boolean ExistUser = userRepository.existsByEmail(userDTO.getEmail());
        if (ExistUser) {
            throw new DuplicateUserException("User with email " + userDTO.getEmail() + " already exists.");
        }

        String password = bCryptPasswordEncoder.encode(userDTO.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .password(password)
                .phone(userDTO.getPhone())
                .imageUrl(userDTO.getImageUrl())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .membershipGrade(userDTO.getMembershipGrade())
                .authority(userDTO.getAuthority())
                .build();

        userRepository.save(userEntity);

        log.info(userEntity);
    }
}



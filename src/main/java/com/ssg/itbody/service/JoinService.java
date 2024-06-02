package com.ssg.itbody.service;

import com.ssg.itbody.dto.UserDTO;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.enums.MembershipGrade;
import com.ssg.itbody.exception.DuplicateUserException;
import com.ssg.itbody.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String uploadPath = "C:/Users/moonj/Downloads/Shopfit_BackEnd-master/IT-body/uploads";

    public void joinProcess(UserDTO userDTO, MultipartFile imgFile) throws IOException {
        log.info("JoinService: joinProcess called with userDTO: {}", userDTO);

        if (imgFile == null || imgFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 필요합니다.");
        }

        boolean existUser = userRepository.existsByEmail(userDTO.getEmail());
        if (existUser) {
            throw new DuplicateUserException("User with email " + userDTO.getEmail() + " already exists.");
        }

        String password = bCryptPasswordEncoder.encode(userDTO.getPassword());

        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + imgFile.getOriginalFilename();
        File profileImg = new File(uploadPath, fileName);
        imgFile.transferTo(profileImg);

        UserEntity userEntity = UserEntity.builder()
                .nickname(userDTO.getNickname())
                .email(userDTO.getEmail())
                .password(password)
                .phone(userDTO.getPhone())
                .gender(userDTO.getGender())
                .healthStatus(userDTO.getHealthStatus())
                .membershipGrade(MembershipGrade.MEMBER)
                .profileImgName(fileName) // 이미지 파일명 설정
                .profileImgPath(uploadPath + "/" + fileName) // 이미지 경로 설정
                .build();

        userRepository.save(userEntity);
        log.info("JoinService: UserEntity saved {}", userEntity);
    }
}
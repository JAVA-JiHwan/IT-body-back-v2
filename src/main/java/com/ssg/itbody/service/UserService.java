package com.ssg.itbody.service;

import com.ssg.itbody.dto.UserResponseDTO;
import com.ssg.itbody.dto.UserUpdateDTO;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.enums.MembershipGrade;
import com.ssg.itbody.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;

    public UserResponseDTO getUserById(Long id) {
        String jpql = "SELECT u FROM UserEntity u WHERE u.userId = :id";
        TypedQuery<UserEntity> query = entityManager.createQuery(jpql, UserEntity.class);
        query.setParameter("id", id);
        UserEntity userEntity = query.getSingleResult();
        return UserResponseDTO.from(userEntity);
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = userRepository.findById(userUpdateDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userUpdateDTO.getUserId()));

        userEntity.setNickname(userUpdateDTO.getNickname());
        userEntity.setPhone(userUpdateDTO.getPhone());
        userEntity.setEmail(userUpdateDTO.getEmail());
        userEntity.setHealthStatus(userUpdateDTO.getHealthStatus());
        userEntity.setProfileImgName(userUpdateDTO.getProfileImgName());
        userEntity.setProfileImgPath(userUpdateDTO.getProfileImgPath());

        userRepository.save(userEntity);
    }

    public byte[] getProfileImage(Long userId) throws IOException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        UserEntity userEntity = userEntityOptional.get();
        String profileImgPath = userEntity.getProfileImgPath();

        InputStream inputStream = new FileInputStream(profileImgPath);
        byte[] imageByteArray = inputStream.readAllBytes();
        inputStream.close();
        return imageByteArray;
    }
}
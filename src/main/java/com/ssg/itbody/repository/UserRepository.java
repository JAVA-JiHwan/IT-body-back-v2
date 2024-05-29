package com.ssg.itbody.repository;

import com.ssg.itbody.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByEmail(String email); // email로 사용자 정보를 가져옴


    boolean existsByEmail(String email);// 이메일 존재하는지

    
}

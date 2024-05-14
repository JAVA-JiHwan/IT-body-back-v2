package com.ssg.itbody.repository;

import com.ssg.itbody.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email); // email로 사용자 정보를 가져옴

    // 닉네임으로 회원 찾기
    UserEntity findByNickname(String nickname);

    boolean existsByEmail(String email);// 이메일 존재하는지
}

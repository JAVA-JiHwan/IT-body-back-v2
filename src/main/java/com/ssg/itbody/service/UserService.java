package com.ssg.itbody.service;



import com.ssg.itbody.dto.AddUserRequestDTO;
import com.ssg.itbody.dto.LoginRequestDTO;
import com.ssg.itbody.dto.LoginResponseDTO;
import com.ssg.itbody.entity.UserEntity;


import java.util.List;

public interface UserService {
    void signup(AddUserRequestDTO dto);
    UserEntity findById(Long userId);


    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    UserEntity createUser(UserEntity userEntity);
    List<UserEntity> getAllUsers();

    UserEntity getUserByNickname(String nickname);

    UserEntity getUserByEmail(String email);

    UserEntity getUserById(Long id);
    void updateUser(UserUpdateDTO userUpdateDTO);

    LoginResponseDTO socialLogin(LoginResponseDTO loginResponseDTO);
    void deleteUser(Long id);

    boolean authenticateUser(LoginRequestDTO loginRequest);





}

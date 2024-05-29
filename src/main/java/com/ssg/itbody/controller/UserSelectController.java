package com.ssg.itbody.controller;

import com.ssg.itbody.dto.UserResponseDTO;
import com.ssg.itbody.dto.UserUpdateDTO;
import com.ssg.itbody.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserSelectController {
    private final UserService userService;

    @GetMapping("/page/{userId}")
    public String myPage() {
        return "mypage";
    }


    // 회원 정보 조회
//    @GetMapping("/page/{userId}")
//    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
//        UserResponseDTO userResponseDTO = userService.getUserById(userId);
//        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
//    }

    // 회원 정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setUserId(userId);
        userService.updateUser(userUpdateDTO);
        return new ResponseEntity<>("유저 정보가 성공적으로 업데이트되었습니다.", HttpStatus.OK);
    }

}
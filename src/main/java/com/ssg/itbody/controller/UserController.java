package com.ssg.itbody.controller;

import com.ssg.itbody.dto.AddUserRequestDTO;
import com.ssg.itbody.dto.LoginRequestDTO;
import com.ssg.itbody.dto.LoginResponseDTO;
import com.ssg.itbody.dto.UserResponseDTO;
import com.ssg.itbody.entity.UserEntity;
import com.ssg.itbody.config.jwt.TokenProvider;
import com.ssg.itbody.config.ouath.DecodeSocialLoginToken;
import com.ssg.itbody.service.UserDetailService;
import com.ssg.itbody.service.UserServiceImpl;
import com.ssg.itbody.service.UserUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userService;
    private final UserDetailService userDetailService;
    private final TokenProvider tokenProvider;
    private final DecodeSocialLoginToken decodeSocialLoginToken;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid AddUserRequestDTO requestDTO) {
        userService.signup(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody String refreshToken) {
        userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponseDTO = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody @Valid String token) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(decodeSocialLoginToken.decodingToken(token));
    }

    @GetMapping("/mypage/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserEntity userEntity = userService.getUserById(id);
        return ResponseEntity.ok(UserResponseDTO.from(userEntity));
    }

    @PatchMapping("/mypage/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setUserId(id);
        userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok("유저 정보가 성공적으로 업데이트되었습니다.");
    }
}
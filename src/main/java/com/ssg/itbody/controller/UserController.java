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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Iterator;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;
    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;

    private final DecodeSocialLoginToken decodeSocialLoginToken;

    @GetMapping("/")
    public String mainP() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller : "+name +role;
    }



    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody AddUserRequestDTO requestDTO){
        userService.signup(requestDTO); // 위 로직 전부 서비스레이어로 옮김
        return ResponseEntity.ok("회원가입 성공");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody String refreshToken){
            userService.logout(accessToken, refreshToken);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponseDTO = userService.login(loginRequest);

        return ResponseEntity.ok(loginResponseDTO);
     }

     @PostMapping("/login/google")
     public ResponseEntity<?> googleLogin(@RequestBody String token) throws GeneralSecurityException, IOException {

         return ResponseEntity.ok(decodeSocialLoginToken.decodingToken(token));
     }


    // 회원 정보 조회
    @GetMapping("/mypage/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserEntity userEntity = userService.getUserById(id);

            UserResponseDTO userResponseDTO = UserResponseDTO.from(userEntity);
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

    // 회원 정보 수정
    @PatchMapping("/mypage/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {

        userUpdateDTO.setUserId(id);
            userService.updateUser(userUpdateDTO);
            return new ResponseEntity<>("유저 정보가 성공적으로 업데이트되었습니다.", HttpStatus.OK);

    }



    // 유저 포인트 사용 시 포인트 수정
    @PatchMapping("/{userId}/usePoints/{usedPoints}")
    public ResponseEntity<String> usePoints(
            @PathVariable Long userId,
            @PathVariable int usedPoints
    ) {
        // userId로 사용자 정보를 조회
        UserEntity userEntity = userService.getUserById(userId);



        return ResponseEntity.ok("포인트가 성공적으로 사용되었습니다.");
    }




}

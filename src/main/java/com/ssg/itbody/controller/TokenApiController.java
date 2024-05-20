//package com.ssg.itbody.controller;
//
//
//import com.ssg.itbody.dto.CreateAccessTokenRequest;
//import com.ssg.itbody.dto.CreateAccessTokenResponse;
//import com.ssg.itbody.service.TokenService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//public class TokenApiController {
//
//    private final TokenService tokenService;
//
//    @PostMapping("/api/token")                                        // json으로 보내야 파라미터 매핑됨
//    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestHeader CreateAccessTokenRequest request){
//        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
//
//        // 발급된 토큰을 201 응답(생성됨) 과 함께 리턴
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new CreateAccessTokenResponse(newAccessToken));
//    }
//}

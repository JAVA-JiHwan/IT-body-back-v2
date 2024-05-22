package com.ssg.itbody.controller;

import com.ssg.itbody.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class UserControllerRe {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/")
    public String mainPage(
    ) {
        // 메인 페이지에서 필요한 정보를 모델에 추가
        return "index"; // main.html 뷰를 반환
    }


}
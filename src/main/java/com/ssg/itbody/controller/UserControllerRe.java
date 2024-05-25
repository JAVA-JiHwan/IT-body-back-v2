package com.ssg.itbody.controller;

import com.ssg.itbody.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@Controller
public class UserControllerRe {

    private final JoinService joinService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping({"/", "/index"})
    public String mainPage() {
        // 메인 페이지에서 필요한 정보를 모델에 추가
        return "index"; // main.html 뷰를 반환
    }


}
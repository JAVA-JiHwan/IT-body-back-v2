package com.ssg.itbody.controller;

import com.ssg.itbody.config.jwt.TokenProvider;
import com.ssg.itbody.config.ouath.DecodeSocialLoginToken;
import com.ssg.itbody.service.UserDetailService;
import com.ssg.itbody.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class UserControllerRe {

    private final UserServiceImpl userService;
    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final DecodeSocialLoginToken decodeSocialLoginToken;

    @GetMapping("/")
    public String mainP() {
        return "Main Controller : ";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "login";
    }
}

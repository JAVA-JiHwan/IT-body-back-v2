//package com.ssg.itbody.controller;
//
//import com.ssg.itbody.config.jwt.TokenProvider;
////import com.ssg.itbody.config.ouath.DecodeSocialLoginToken;
//import com.ssg.itbody.dto.AddUserRequestDTO;
//import com.ssg.itbody.service.UserDetailService;
//import com.ssg.itbody.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@RequiredArgsConstructor
//@Controller
//public class UserControllerRe {
//
//    private final UserService userService;
//    private final UserDetailService userDetailService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final TokenProvider tokenProvider;
////    private final DecodeSocialLoginToken decodeSocialLoginToken;
//
//    @GetMapping("/")
//    public String mainPage(Model model) {
//        // 메인 페이지에서 필요한 정보를 모델에 추가
//        model.addAttribute("message", "Welcome to Main Page");
//        return "index"; // main.html 뷰를 반환
//    }
//
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login"; // login.html 뷰를 반환
//    }
//
//
//
//    @PostMapping("/signup")
//    public String signup(AddUserRequestDTO requestDTO) {
//        userService.signup(requestDTO);
//        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
//    }
////
////    @PostMapping("/login")
////    public String login(HttpServletRequest request, LoginRequestDTO loginRequest, Model model) {
////        try {
////            LoginResponseDTO loginResponseDTO = userService.login(loginRequest);
////            HttpSession session = request.getSession(true);
////            session.setAttribute("user", loginResponseDTO);
////            return "redirect:/"; // 로그인 후 메인 페이지로 리다이렉트
////        } catch (Exception e) {
////            model.addAttribute("error", "Invalid email or password.");
////            return "login"; // 로그인 실패 시 다시 로그인 페이지로
////        }
////    }
////
////    @GetMapping("/logout")
////    public String logout(HttpServletRequest request, HttpServletResponse response) {
////        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
////        HttpSession session = request.getSession(false);
////        if (session != null) {
////            session.invalidate();
////        }
////        return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
////    }
////}
//}
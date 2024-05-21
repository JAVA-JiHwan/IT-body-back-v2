package com.ssg.itbody.controller;

import com.ssg.itbody.dto.UserDTO;
import com.ssg.itbody.exception.DuplicateUserException;
import com.ssg.itbody.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/login")
public class UserController {

    private final UserService userService;

    //    private final UserDetailService userDetailService;


    @GetMapping("/login")
    public String login(@RequestParam(value = "expired", required = false) String expired,
                        @RequestParam(value = "error", required = false) String error, @RequestParam(value = "exception", required = false) String exception, Model model) {
        if ("true".equals(expired)) {
            model.addAttribute("message", "다른 곳에서 접근되었습니다, 연결을 종료합니다");
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:login/login";
    }


    //    //회원 가입 processing 이메일이 중복된 경우 이미 존재하는 회원 반환
//    @PostMapping(value = "/joinProc", produces = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity<String> joinProcess(@ModelAttribute UserDTO userDTO) {
//        log.info(userDTO.getNickname());
//        try {
//            userService.joinProcess(userDTO);
//            return ResponseEntity.ok("회원가입이 완료되었습니다.");
//        } catch (DuplicateUserException d) {
//            return ResponseEntity.badRequest().body("이미 존재하는 회원입니다.");
//        }
//    }
    @PostMapping("/joinProc")
    public String joinProcess(@ModelAttribute UserDTO userDTO, Model model) {
        log.info(userDTO.getNickname());
        try {
            userService.joinProcess(userDTO);
            return "redirect:/login/login"; // 회원가입 후 로그인 페이지로 리다이렉트
        } catch (DuplicateUserException d) {
            model.addAttribute("error", "이미 존재하는 회원입니다.");
            return "login/login"; // 회원가입 페이지로 다시 돌아가기
        }
    }

    @PostMapping("/loginProc")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model) {
        // 여기에 로그인 처리 로직을 추가하세요.
        // 예: 사용자 인증, 세션 설정 등.

        // 성공적으로 로그인한 경우 리다이렉트할 페이지.
        return "redirect:/index";
    }
}

package com.ssg.itbody.controller;


import com.ssg.itbody.dto.UserDTO;
import com.ssg.itbody.exception.DuplicateUserException;
import com.ssg.itbody.service.JoinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;

    //로그인 페이지 라우팅,에러 시 에러 처리한 문구 실행
    @GetMapping("/login")
    public String login(@RequestParam(value = "expired", required = false) String expired,
                        @RequestParam(value = "error", required = false) String error, @RequestParam(value = "exception", required = false) String exception, Model model) {
        if ("true".equals(expired)) {
            model.addAttribute("message", "다른 곳에서 접근되었습니다, 연결을 종료합니다");
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login/login";
    }

    //회원 가입 페이지 라우팅
    @GetMapping("/join")
    public String join() {
        log.info("UserController join");
        return "login/join";
    }


    //회원 가입 processing 이메일이 중복된 경우 이미 존재하는 회원 반환
    @PostMapping(value = "/joinProc", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> joinProcess(@ModelAttribute UserDTO userDTO) {
        log.info(userDTO.getNickname());
        try {
            joinService.joinProcess(userDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (DuplicateUserException d) {
            return ResponseEntity.badRequest().body("이미 존재하는 회원입니다.");
        }
    }

//    @PostMapping("/loginProc")
//    public String loginProcess(@RequestParam String email, @RequestParam String password, Model model) {
//        boolean isAuthenticated = joinService.authenticate(email, password);
//        if (isAuthenticated) {
//            // 인증 성공 시, 세션 등에 사용자 정보를 저장하는 로직을 추가할 수 있습니다.
//            return "redirect:/index"; // 로그인 성공 시 리디렉션할 페이지
//        } else {
//            model.addAttribute("error", "아이디나 비밀번호가 잘못되었습니다.");
//            return "login/login"; // 로그인 실패 시 다시 로그인 페이지로
//        }
//    }
}


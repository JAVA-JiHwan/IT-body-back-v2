package com.ssg.itbody.controller;

import com.ssg.itbody.dto.UserDTO;
import com.ssg.itbody.exception.DuplicateUserException;
import com.ssg.itbody.service.JoinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@Log4j2
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;


    @GetMapping("/login")
    public String login(@RequestParam(value = "expired", required = false) String expired,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
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

    @GetMapping("/join")
    public String join() {
        log.info("UserController join");
        return "login/join";
    }

    @PostMapping(value = "/joinProc", produces = MediaType.TEXT_PLAIN_VALUE)
    public void joinProcess(@ModelAttribute UserDTO userDTO, @RequestParam(value = "imgFile") MultipartFile imgFile, HttpServletResponse response) throws IOException {
        log.info(userDTO.getNickname());
        try {
            if (imgFile.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이미지가 존재하지 않습니다.");
                return;
            }
            joinService.joinProcess(userDTO, imgFile);
            response.sendRedirect("/login/login"); // 회원가입 성공 시 로그인 페이지로 리다이렉트
        } catch (DuplicateUserException d) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이미 존재하는 회원입니다.");
        } catch (IOException e) {
            log.error("회원가입 중 오류 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "회원가입 중 오류가 발생했습니다.");
        }
    }


}




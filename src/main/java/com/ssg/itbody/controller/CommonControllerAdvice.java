package com.ssg.itbody.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


//이 클래스는 모든 컨트롤러에서 매핑되기전 고정된 값인 사이드바나 navbar에 사용자의 정보를 불러오기 위해 AOP 사용
@ControllerAdvice
public class CommonControllerAdvice {
    @ModelAttribute
    public void addCommonAttributes(Model model) {
//        String id = SecurityContextHolder.getContext().getAuthentication().getName();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String membershipGrade = authentication.getAuthorities().iterator().next().getAuthority();
//        model.addAttribute("userId", id);
//        model.addAttribute("membershipGrade", membershipGrade);
        // SecurityContext에서 인증된 사용자의 이름(일반적으로 username 또는 email)을 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String name = authentication.getName(); // 사용자 이메일
            String membershipGrade = authentication.getAuthorities().iterator().next().getAuthority(); // 첫 번째 권한을 가져옴

            model.addAttribute("nickname", name);
            model.addAttribute("membershipGrade", membershipGrade);
        }


    }
}

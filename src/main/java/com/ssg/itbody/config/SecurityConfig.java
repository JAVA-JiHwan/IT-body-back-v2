package com.ssg.itbody.config;

import com.ssg.itbody.exception.CustomAuthFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.PrintWriter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final AuthenticationFailureHandler customAuthFailureHandler = new CustomAuthFailureHandler();


    //권한별 계층 설정 추후 리펙토링 용이 및 구조 보기 쉽게 해놓음
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        roleHierarchy.setHierarchy("TRAINER > MEMBER");

        return roleHierarchy;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/assets/css/**", "/static/assets/js/**", "/static/assets/img/**", "/static/assets/lib/**", "/static/assets/vendor/**", "/static/assets/image/**", "/uploads/**").permitAll()
                        .requestMatchers("/static/assets/vendor/js/**", "/static/assets/vendor/css/**").permitAll()
                        .requestMatchers("/layout", "/expired").permitAll()
                        .requestMatchers("/login/login", "/login/join", "/login/loginProc", "/login/joinProc", "/login/logout", "/mypage/**").permitAll()
                        .requestMatchers("/index", "/reserve", "/reserve/create").permitAll()
                        .anyRequest().hasAnyRole("트레이너", "회원")


                );
//        로그인 처리
        http
                .formLogin((auth) ->
                        auth.loginPage("/login/login")
                                .loginProcessingUrl("/login/loginProc")//로그인 처리
                                .defaultSuccessUrl("/") //로그인 성공 시 메인 화면으로 리다이렉션
                                .failureHandler(customAuthFailureHandler) //로그인 실패 시 Failure Handler 통해 처리
                                .permitAll() //로그인 페이지는 인증 없이 접근 가능
                );

        http
                .logout((auth -> auth.logoutUrl("/logout") //로그아웃 처리 URL
                        .logoutSuccessUrl("/login/login") // 로그아웃 성공 시 메인 화면으로 리다이렉션
                        .invalidateHttpSession(true)// 세션 무효화
                        .deleteCookies("JSESSIONID") //로그아웃 시 JSESSIONID 삭제
                ));
        //세션 설정 및 중복 로그인 처리
        http
                .sessionManagement((auth ->
                        auth.invalidSessionUrl("/") //세션이 유효하지 않을 때 해당 페이지로 이동
                                .maximumSessions(1) // 최대 동시 허용 가능 세션 -1 일 경우 무제한
                                .maxSessionsPreventsLogin(false) //동시 로그인 차단
                                .expiredUrl("/login?expired=true"))); //세션 만료 후 이동 페이지

        // 세션 고정 보호 정책 설정: 새로운 세션 ID를 발급하여 세션 고정 공격 방지
        http
                .sessionManagement((auth -> auth
                        .sessionFixation().changeSessionId()));

        //접근 권한 거부에 대한 accessDeniedHandler 처리
        http
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.accessDeniedHandler(accessDeniedHandler()));

//        http.csrf((csrf) -> csrf.disable());

        return http.build();
    }

    //접근 거부 핸들러 403 forbidden 작동시 경고창 실행
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            // 자바 스크립트 경고창 띄우기
            out.println("<html><head><script>alert('권한이 없습니다.'); location.href='/';</script></head><body></body></html>");
            out.flush();
        });

    }

}
package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)    // @PreAuthorize 사용하기 위해 설정
public class SecurityConfig { // 스프링 시큐리티 (웹 어플리케이션 보안) 설정

    // 필터 체인 생성
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 인증되지 않은 모든 페이지의 요청 허락
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))) // '/h2-console/'로 시작하는 모든 url은 검증 하지 않음
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) // X-Frame-Options 헤더의 기본값 DENY -> SAMEORIGIN으로 변경
                .formLogin((formLogin) -> formLogin // 로그인 설정 담당
                        .loginPage("/user/login")   // 로그인 페이지 url
                        .defaultSuccessUrl("/"))    // 로그인 성공 시 default 페이지로 이동
                .logout((logout) -> logout  // 로그아웃 설정 담당
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))    // 로그아웃 페이지 url
                        .logoutSuccessUrl("/")  // 로그아웃 성공 시 default 페이지로 이동
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 사용자 인증
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
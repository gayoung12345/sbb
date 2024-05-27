package com.mysite.sbb.user;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form"; // 회원가입 폼 가져옴
    }

    // 회원 가입 완료(post)
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // 오류 발생
            return "signup_form"; // 작성하던 회원가입 폼 페이지에 남아 있음
        }

        // 만약 비밀번호1과 비밀번호2가 다를 경우
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다."); // 오류 메세지 출력
            return "signup_form"; // 작성하던 회원가입 폼 페이지에 남아 있음
        }

        try{
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());  // 회원가입
        }catch(DataIntegrityViolationException e) { // 중복 아이디나 중복 이메일로 가입하려는 경우 생기는 exception
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form"; // 작성하던 회원가입 폼 페이지에 남아 있음
        }catch(Exception e) { // 이외의 다른 exception
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form"; // 작성하던 회원가입 폼 페이지에 남아 있음
        }

        // 오류 없이 회원가입이 완료 된 경우
        return "redirect:/";    // default 페이지로 돌아감
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login_form";    // 로그인 폼 html 가져옴
    }

}
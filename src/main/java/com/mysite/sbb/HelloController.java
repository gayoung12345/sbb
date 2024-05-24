package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/jump") // call localhost:8080/hello (8080은 기본포트)
    @ResponseBody // 메소드의 출력값 그대로 return
    public String hello(){
        return "점프 투 스프링 부트";
    }

}

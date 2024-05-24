package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    // mapping : 어떤 값을 다른 값에 대응시키는 과정
    @GetMapping("/sbb") // http://localhost:8080/sbb을 호출 했을 때 연결되는 메소드로 지정
    @ResponseBody // 메소드 호출 시, 자신을 return
    public String index() {
        System.out.println("index");
        return "index";
    }

    @GetMapping("/")
    public String root(){
        return "redirect:/question/list";
    }
}

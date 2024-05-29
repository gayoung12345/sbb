package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/question") // 클라이언트의 요청(url)에 맞는 클래스나 메서드를 연결
@RequiredArgsConstructor
@Controller
public class QuestionController {

    // private final QuestionRepository questionRepository;
    private final QuestionService questionService;  // controller -> service -> repository 순으로 요청
    private final UserService userService;

    // 질문 전체 리스트
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list"; // 질문 전체 리스트 html 가져옴
    }

    // 질문 상세 페이지
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail"; // 질문 상세 페이지 html 가져옴
    }

    // 질문 작성 페이지
    @PreAuthorize("isAuthenticated()")  // 로그인이 필요한 메소드
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form"; // 질문 작성 폼 html 가져옴
    }

    // 질문 작성 완료(post)
    // @PreAuthorize("isAuthenticated()")  // 로그인이 필요한 메소드
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 'ROLE_ADMIN'권한이 있는 사용자만 글 작성 가능
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()) { // 오류 발생
            return "question_form"; // 이전에 작성하던 질문 작성 페이지에 남아 있음
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";   // 질문 작성을 완료하면 질문 전체 리스트 페이지로 돌아감
    }

    // 질문 수정
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) { // 작성자 != 로그인 사용자
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); // 오류 출력
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    // 질문 수정 완료(post)
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) { // 작성자 != 로그인 사용자
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); // 오류 출력
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent()); // 질문 수정
        return String.format("redirect:/question/detail/%s", id); // 질문 수정을 완료하면 질문 상세 페이지로 돌아감
    }

    // 질문 삭제
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) { // 작성자 != 로그인 사용자
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다."); // 오류 출력
        }
        this.questionService.delete(question); // 질문 삭제
        return "redirect:/"; // 질문 삭제를 완료하면 default 페이지로 돌아감
    }

    // 질문 추천
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}

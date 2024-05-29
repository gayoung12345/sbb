package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer") // 클라이언트의 요청(url)에 맞는 클래스나 메서드를 연결
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    // 답변 작성 완료(post)
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) { // 에러 발생 시
            model.addAttribute("question", question);
            return "question_detail";   // 이전에 작성하던 페이지를 유지함
        }
        Answer answer = this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),answer.getId()); // 답변 작성을 완료하면 질문 상세 페이지로 돌아감
    }

    // 답변 수정
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {    // 작성자 != 로그인 사용자
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); // 에러 메세지 출력
        }
        answerForm.setContent(answer.getContent()); // 답변 수정폼을 가져옴
        return "answer_form"; // 답변 작성 폼 호출
    }

    // 답변 수정 완료(post)
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) { // bindingResult: 검증오류 발생 시 오류 내용을 보관하는 객체
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) { // 작성자 != 로그인 사용자
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); // 에러 메세지 출력
        }
        this.answerService.modify(answer, answerForm.getContent()); // 수정
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId()); // 수정 완료 후 원래 있던 질문 상세 페이지로 돌아감
    }

    // 답변 삭제
    @PreAuthorize("isAuthenticated()") // 로그인이 필요한 메소드
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) { // 작성자 != 로그인 사용자
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다."); // 에러 메세지 출력
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId()); // 삭제 완료 후 원래 있던 질문 상세 페이지로 돌아감
    }

    // 답변 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),answer.getId());
    }

}

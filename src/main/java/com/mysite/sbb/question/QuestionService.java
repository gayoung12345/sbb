package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 제목 리스트 가져오기
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));   // 생성일 순으로 내림차 정렬
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));   // 한 페이지에 10개의 제목이 보이도록 페이징
        Specification<Question> spec = search(kw);  // kw:keyword
        // return this.questionRepository.findAll(spec, pageable);
        return this.questionRepository.findAllByKeyword(kw,pageable);
    }

    // 질문 상세 가져오기
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
           throw new DataNotFoundException("question not found");   // 질문이 삭제되었거나 하는 이유로 없으면 오류 출력
        }
    }

    // 질문 작성
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    // 질문 수정
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    // 질문 삭제
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    // 추천인 저장
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    // 검색
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                                        // q: Question엔티티의 객체
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                // u1: question과 siteUser를 outer join한 siteUser 객체, 질문 작성자 검색에 활용
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                // a: question과 answer를 outer join한 answer 객체, 답변 내용 검색에 활용
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                // u2: a와 siteUser를 outer join한 siteUser 객체, 답변 작성자 검색에 활용

                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

}

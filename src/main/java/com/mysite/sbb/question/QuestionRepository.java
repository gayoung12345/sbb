package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    // interface : view
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // select subject from Question
    Question findBySubject(String subject);
    // select subject, content from Question
    Question findBySubjectAndContent(String subject, String content);
    // select * from Question where subject like %sbb
    List<Question> findBySubjectLike(String subject);
    // select * from Question
    Page<Question> findAll(Pageable pageable);
}

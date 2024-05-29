package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // select * from Question, Answer, SiteUser where ?
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    // select * from question, answer, siteUser where ?
    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

}

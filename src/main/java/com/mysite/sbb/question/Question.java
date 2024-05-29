package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity // @Entity: DB 테이블에 대응하는 하나의 클래스. JPA가 관리해주며, JPA를 사용해서 DB 테이블과 매핑할 수 있는 클래스 임을 알려주는 Annotation
public class Question { // DTO(Data Transfer Object) : 클라이언트와 서버가 데이터를 주고받을 때 사용하는 객체 Model

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SEQUENCE
    private Integer id; // 생성 아이디

    @Column(length = 200)
    private String subject; // 질문 제목

    @Column(columnDefinition = "text")
    private String content; // 질문 내용

    private LocalDateTime createDate;   // 작성일

    // 1:N
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) // 만약 질문이 삭제되면 답변도 같이 삭제 (cascade)
    private List<Answer> answerList;    // 답변 리스트

    // N:1
    @ManyToOne
    private SiteUser author;    // 작성자

    // 수정일
    private LocalDateTime modifyDate;

    // 추천수
    @ManyToMany
    Set<SiteUser> voter;
}

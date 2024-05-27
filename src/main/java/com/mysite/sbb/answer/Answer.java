package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity // @Entity: DB 테이블에 대응하는 하나의 클래스. JPA가 관리해주며, JPA를 사용해서 DB 테이블과 매핑할 수 있는 클래스 임을 알려주는 Annotation
public class Answer { // DTO(Data Transfer Object) : 클라이언트와 서버가 데이터를 주고받을 때 사용하는 객체 Model

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SEQUENCE
    private Integer id;  // 생성 아이디

    @Column(columnDefinition = "text")
    private String content; // 답변 내용

    @CreatedDate
    private LocalDateTime createDate; // 작성일

    // N:1
    @ManyToOne
    private Question question; // 연결된 질문

    // N:1
    @ManyToOne
    private SiteUser author;    // 작성자

    // 수정일
    private LocalDateTime modifyDate;

}

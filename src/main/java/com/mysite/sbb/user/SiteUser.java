package com.mysite.sbb.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // @Entity: DB 테이블에 대응하는 하나의 클래스. JPA가 관리해주며, JPA를 사용해서 DB 테이블과 매핑할 수 있는 클래스 임을 알려주는 Annotation
public class SiteUser { // DTO(Data Transfer Object) : 클라이언트와 서버가 데이터를 주고받을 때 사용하는 객체 Model

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SEQUENCE
    private Long id; // 생성 아이디

    @Column(unique = true)
    private String username; // 사용자 아이디

    private String password; // 사용자 비밀번호

    @Column(unique = true)
    private String email; // 사용자 이메일주소
}
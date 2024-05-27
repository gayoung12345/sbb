package com.mysite.sbb.answer;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {   // 답변 작성 폼
    // @NotEmpty: Null과 공백("")을 허용하지 않음. 문제 발생 시 메세지 출력
    // 단, " "(space)는 가능
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
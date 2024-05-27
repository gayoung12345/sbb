package com.mysite.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    // 데이터를 못 찾는 오류 발생 시 메세지 출력
    public DataNotFoundException(String message) {
        super(message);
    }

}

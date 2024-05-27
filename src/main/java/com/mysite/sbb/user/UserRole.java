package com.mysite.sbb.user;

import lombok.Getter;


@Getter // 상수 값을 변경 할 필요가 없으므로 @Setter는 사용하지 않음
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
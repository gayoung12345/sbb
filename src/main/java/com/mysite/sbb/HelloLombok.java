package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// @Setter
@RequiredArgsConstructor
@Getter
public class HelloLombok {
    private final String hello;
    private final int lombok;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok("안녕", 5);
        // helloLombok.setHello("안녕");
        // helloLombok.setLombok(5);

        System.out.println(helloLombok.getHello());
        System.out.println(helloLombok.getLombok());
    }
}

package com.mysite.sbb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private String title;
    private String author;

    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("jump to springboot3");
        book.setAuthor("park");

        System.out.println(book.getTitle());
        System.out.println(book.getAuthor());
    }
}

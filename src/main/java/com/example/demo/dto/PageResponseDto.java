package com.example.demo.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class PageResponseDto <T>{
    private List<T> content;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageResponseDto(List<T> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page + 1;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}

package com.board.boardchallenge.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String writer;
    private String title;
    private String contents;
    private String regdate;
}

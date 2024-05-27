package com.board.boardchallenge.dto;

import com.board.boardchallenge.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long board_id;
    private String writer;
    private String title;
    private String contents;
    private String regdate;

    public BoardResponseDto(Board board) {
        this.board_id = board.getBoard_id();
        this.writer = board.getWriter();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.regdate = board.getRegdate();
    }

    public BoardResponseDto(Long boardId, String writer, String title, String contents, String regdate) {
        this.board_id = boardId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.regdate = regdate;
    }
}

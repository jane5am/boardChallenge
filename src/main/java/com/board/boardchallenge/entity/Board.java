package com.board.boardchallenge.entity;

import com.board.boardchallenge.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter //게터
@Setter //세터
@NoArgsConstructor //기본생성자 만들어주는 어노테이션
public class Board {
    private Long board_id;
    private String writer;
    private String title;
    private String contents;
    private String regdate;

    public Board(BoardRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.regdate = requestDto.getRegdate();
    }

    public void update(BoardRequestDto requestDto) {
        this.writer = requestDto.getWriter();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.regdate = requestDto.getRegdate();
    }

}

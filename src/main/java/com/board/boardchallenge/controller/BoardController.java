package com.board.boardchallenge.controller;

import com.board.boardchallenge.dto.BoardRequestDto;
import com.board.boardchallenge.dto.BoardResponseDto;
import com.board.boardchallenge.service.BoardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final JdbcTemplate jdbcTemplate;

    public BoardController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 글목록 불러오기
    @GetMapping("/get")
    public List<BoardResponseDto> getBoard() {
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.getBoard();
    }

    //글생성
    @PostMapping("/create")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.createBoard(requestDto);
    }

    // 글삭제
    @DeleteMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        BoardService boardService = new BoardService(jdbcTemplate);
        return id + boardService.deleteBoard(id);
    }

    //글 수정
    @PutMapping("/update/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) { //Long인 이유 : id만 반환할거라
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.updateBoard(id, requestDto);
    }

    @PostMapping("/search")
    public String searchBoard() {
        return "search";
    }




}

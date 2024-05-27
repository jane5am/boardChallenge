package com.board.boardchallenge.controller;

import com.board.boardchallenge.dto.BoardRequestDto;
import com.board.boardchallenge.dto.BoardResponseDto;
import com.board.boardchallenge.entity.Board;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {

    long iiid = 0;

    @GetMapping("/get")
    public String getBoard() {
        return "get";
    }

    //글생성
    @PostMapping("/create")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto){
        // requestDto를 Entity로 변환해야한다 왜? 데이터베이스와 소통하는게 Entity니까
        Board board = new Board(requestDto);

        // 글 id의 MAX값 찾기
        Long maxId = ++iiid;
        board.setBoard_id(maxId);

        //DB저장

        // Entity를 responseDto로 변환
        BoardResponseDto responseDto = new BoardResponseDto(board);

        return responseDto;

    }

    @GetMapping("/delete")
    public String deleteBoard() {
        return "delete";
    }

    @PostMapping("/modify")
    public String modifyBoard() {
        return "modify";
    }

    @PostMapping("/search")
    public String searchBoard( ) {
        return "search";
    }

}

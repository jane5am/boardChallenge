package com.board.boardchallenge.service;

import com.board.boardchallenge.dto.BoardRequestDto;
import com.board.boardchallenge.dto.BoardResponseDto;
import com.board.boardchallenge.entity.Board;
import com.board.boardchallenge.repository.BoardRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class BoardService {

    private final JdbcTemplate jdbcTemplate;

    public BoardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 글생성
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {

        // requestDto를 Entity로 변환해야한다 왜? 데이터베이스와 소통하는게 Entity니까
        Board board = new Board(requestDto);

        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);
        Board saveBoard = boardRepository.save(board);

        // Entity를 responseDto로 변환
        BoardResponseDto responseDto = new BoardResponseDto(board);

        return responseDto;
    }

    // 글목록 가져오기
    public List<BoardResponseDto> getBoard() {

        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);
        return boardRepository.findAll();
    }

    // 글 수정
    public Long updateBoard(Long id, BoardRequestDto requestDto) {

        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);

        // 해당 메모가 DB에 존재하는지 확인
        Board board = boardRepository.findById(id);

        if(board != null) {
            // board 내용 수정
            boardRepository.update(id, requestDto);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다");
        }
    }


    // 글 삭제
    public String deleteBoard(Long id) {

        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);

        // 해당 메모가 DB에 존재하는지 확인
        Board board = boardRepository.findById(id);

        if(board != null) {
            // board 삭제
            boardRepository.delete(id);
            return "삭제완료";
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다");
        }

    }





}

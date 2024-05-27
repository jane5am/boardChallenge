package com.board.boardchallenge.controller;

import com.board.boardchallenge.dto.BoardRequestDto;
import com.board.boardchallenge.dto.BoardResponseDto;
import com.board.boardchallenge.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        // DB 조회
        String sql = "SELECT * FROM board";

        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long board_id = rs.getLong("board_id");
                String writer = rs.getString("writer");
                String title = rs.getString("title");
                String contents = rs.getString("contents");
                String regdate = rs.getString("regdate");
                return new BoardResponseDto(board_id, writer, title, contents, regdate);
            }
        });
    }

    //글생성
    @PostMapping("/create")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        // requestDto를 Entity로 변환해야한다 왜? 데이터베이스와 소통하는게 Entity니까
        Board board = new Board(requestDto);

        //DB저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO board (writer, title, contents, regdate) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, board.getWriter());
                    preparedStatement.setString(2, board.getTitle());
                    preparedStatement.setString(3, board.getContents());
                    preparedStatement.setString(4, board.getRegdate());
                    return preparedStatement;
                },
                keyHolder);
        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        board.setBoard_id(id);

        // Entity를 responseDto로 변환
        BoardResponseDto responseDto = new BoardResponseDto(board);

        return responseDto;

    }

    // 글삭제
    @DeleteMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {

        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);

        if(board != null) {
            // board 삭제
            String sql = "DELETE FROM board WHERE board_id = ?";
            jdbcTemplate.update(sql, id);

            return "삭제완료";
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다");
        }
    }

    //글 수정
    @PutMapping("/update/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) { //Long인 이유 : id만 반환할거라
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);

        if(board != null) {
            // board 내용 수정
            String sql = "UPDATE board SET writer = ?, title = ?, contents = ?, regdate = ? WHERE board_id = ?";
            jdbcTemplate.update(sql, requestDto.getWriter(), requestDto.getTitle(), requestDto.getContents(), requestDto.getRegdate(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다");
        }
    }

    @PostMapping("/search")
    public String searchBoard() {
        return "search";
    }

    // 찾는 id번호 있는지 확인
    private Board findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE board_id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Board board = new Board();
                board.setWriter(resultSet.getString("writer"));
                board.setTitle(resultSet.getString("title"));
                board.setContents(resultSet.getString("contents"));
                board.setRegdate(resultSet.getString("regdate"));

                return board;
            } else {
                return null;
            }
        }, id);
    }


}

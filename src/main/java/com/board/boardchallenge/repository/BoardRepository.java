package com.board.boardchallenge.repository;

import com.board.boardchallenge.dto.BoardRequestDto;
import com.board.boardchallenge.dto.BoardResponseDto;
import com.board.boardchallenge.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BoardRepository {

    private final JdbcTemplate jdbcTemplate;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Board save(Board board) {

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

        return board;
    }


    public List<BoardResponseDto> findAll() {
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



    // 찾는 id번호 있는지 확인
    public Board findById(Long id) {
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

    public void update(Long id, BoardRequestDto requestDto) {
        String sql = "UPDATE board SET writer = ?, title = ?, contents = ?, regdate = ? WHERE board_id = ?";
        jdbcTemplate.update(sql, requestDto.getWriter(), requestDto.getTitle(), requestDto.getContents(), requestDto.getRegdate(), id);

    }

    public void delete(Long id) {
        // board 삭제
        String sql = "DELETE FROM board WHERE board_id = ?";
        jdbcTemplate.update(sql, id);
    }
}

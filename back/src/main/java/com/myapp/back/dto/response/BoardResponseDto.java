package com.myapp.back.dto.response;

import com.myapp.back.model.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardResponseDto {

    private Long id;
    private String title;
    private String contents;
    private String author;
    private LocalDateTime createTime;

    @Builder
    public BoardResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.author = board.getAuthor();
        this.createTime = board.getCreateTime();
    }

    public BoardResponseDto(Optional<Board> byId) {
    }
}

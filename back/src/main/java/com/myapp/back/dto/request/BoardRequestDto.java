package com.myapp.back.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {
    private Long id;
    private String title;
    private String contents;
    private String author;
    private LocalDateTime createTime;
}

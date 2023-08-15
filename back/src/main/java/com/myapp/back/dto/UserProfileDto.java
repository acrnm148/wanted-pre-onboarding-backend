package com.myapp.back.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    private String email;
    private String refreshToken;
    private LocalDateTime createTime;
}

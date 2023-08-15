package com.myapp.back.dto.response;

import com.myapp.back.auth.jwt.refreshToken.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinResponseDto {
    private Long id;
    private String email;

    private LocalDateTime createTime;
}

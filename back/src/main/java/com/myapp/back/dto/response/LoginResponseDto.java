package com.myapp.back.dto.response;

import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String email;
    private String accessToken;
    private String refreshToken;
}

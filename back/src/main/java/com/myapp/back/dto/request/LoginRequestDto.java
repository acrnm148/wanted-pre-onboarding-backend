package com.myapp.back.dto.request;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    private String email;
    private String password;
}

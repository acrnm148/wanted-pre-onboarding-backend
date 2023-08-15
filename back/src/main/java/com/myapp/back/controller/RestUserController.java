package com.myapp.back.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.myapp.back.auth.jwt.JwtToken;
import com.myapp.back.auth.jwt.service.JwtService;
import com.myapp.back.dto.request.JoinRequestDto;
import com.myapp.back.dto.request.LoginRequestDto;
import com.myapp.back.dto.response.JoinResponseDto;
import com.myapp.back.dto.response.LoginResponseDto;
import com.myapp.back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Tag(name = "RestUserController", description = "유저 API")
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class RestUserController {

    private final JwtService jwtService;
    private final UserService userService;

    /**
     * 과제 1. 사용자 회원가입 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : JoinRequestDto.java
     */
    @Operation(summary = "join", description = "회원가입")
    @Parameter(description = "JoinRequestDto")
    @PostMapping("/join")
    public ResponseEntity<JoinResponseDto> join(@RequestBody JoinRequestDto requestDto){
        log.info("join: {}", requestDto);
        JoinResponseDto responseDto = userService.register(requestDto);
        if (responseDto == null) {
            log.error("failed to join");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(responseDto);
   }

    /**
     * 과제 2. 사용자 로그인 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @Operation(summary = "login", description = "로그인")
    @Parameter(description = "LoginRequestDto")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        log.info("login: {}", requestDto);
        LoginResponseDto responseDto = userService.login(requestDto);
        if (responseDto == null) {
            log.error("user does not exist!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(responseDto);
    }

    /**
     * refresh token 재발급
     * @Author : acrnm148@gmail.com
     * @Param : userid
     * @Param : refreshToken
     */
    @Operation(summary = "get refresh token", description = "refresh token 재발급")
    @Parameter(description = "userid")
    @GetMapping("/refresh/{email}")
    public ResponseEntity<Map<String,String>> getRefreshToken(@PathVariable("email") String email,
                                                           @RequestHeader("refreshToken") String refreshToken,
                                                           HttpServletResponse response) throws JsonProcessingException {
        log.info("getRefreshToken: {}, {}", email, refreshToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        if (refreshToken == null || refreshToken.length() < 7) {
            log.info("token is wrong!");
            return ResponseEntity.badRequest().body(null);
        }

        JwtToken jwtToken = jwtService.validRefreshToken(email, refreshToken);
        Map<String, String> jsonResponse = jwtService.recreateTokenResponse(jwtToken);

        return ResponseEntity.ok().body(jsonResponse);
    }
}

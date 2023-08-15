package com.myapp.back.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.myapp.back.auth.jwt.JwtProperties;
import com.myapp.back.auth.jwt.refreshToken.RefreshToken;
import com.myapp.back.auth.jwt.refreshToken.RefreshTokenRepository;
import com.myapp.back.auth.jwt.service.JwtProviderService;
import com.myapp.back.dto.request.JoinRequestDto;
import com.myapp.back.dto.request.LoginRequestDto;
import com.myapp.back.dto.response.JoinResponseDto;
import com.myapp.back.dto.response.LoginResponseDto;
import com.myapp.back.model.User;
import com.myapp.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 실제 JWT 토큰과 관련된 서비스
 * refresh 토큰을 검사 -> 유효하면 access
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final JwtProviderService jwtProviderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private static final String EMAIL_CODE = "@";
    private static final String NEW_EMAIL_CODE = "WANTEDPREONBOARDING0816";


    /**
     * 회원가입
     * @Author : acrnm148@gmail.com
     * @Param : JoinRequestDto.java
     */
    @Transactional
    public JoinResponseDto register(JoinRequestDto requestDto) {
        log.info("register: {}", requestDto);
        // 이메일 유효성 검사
        if (!validateEmail(requestDto.getEmail())) {
            log.error("its not email");
            return null;
        }
        // 비밀번호 유효성 검사
        if (!validatePassword(requestDto.getPassword())) {
            log.error("short password, make it at least 8 characters long");
            return null;
        }
        //회원가입
        User user = userRepository.save(
                User.builder()
                        .email(requestDto.getEmail())
                        .password(bCryptPasswordEncoder.encode(requestDto.getPassword()))
                        .createTime(LocalDateTime.now())
                        .build());

        return JoinResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createTime(user.getCreateTime())
                .build();
    }

    /**
     * 로그인
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        log.info("login: {}", requestDto);
        // 이메일 유효성 검사
        if (!validateEmail(requestDto.getEmail())) {
            log.error("its not email");
            return null;
        }
        // 비밀번호 유효성 검사
        if (!validatePassword(requestDto.getPassword())) {
            log.error("short password, make it at least 8 characters long");
            return null;
        }
        
        //로그인
        User user = userRepository.findByEmail(requestDto.getEmail());
        if (user == null) {
            return null;
        }
        //비밀번호 불일치
        if (!bCryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            log.error("failed to login");
            return null;
        }
        String newRefreshToken = jwtProviderService.createRefreshToken(user.getId(), user.getEmail());
        RefreshToken refreshToken = new RefreshToken(newRefreshToken);
        user.updateRefreshToken(refreshToken);

        return LoginResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .accessToken(jwtProviderService.createAccessToken(user.getId(),user.getEmail()))
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * 이메일 조건 체크
     * @Author : acrnm148@gmail.com
     * @Param : accessToken
     */
    private boolean validateEmail(String email) {
        if (email.contains("@")) {
            return true;
        }
        return false;
    }

    /**
     * 비밀번호 조건 체크
     * @Author : acrnm148@gmail.com
     * @Param : accessToken
     */
    private boolean validatePassword(String password) {
        if (password.length() >= 8) {
            return true;
        }
        return false;
    }

    /**
     * accesstoken 복호화해서 유저 아이디 추출
     * @Author : acrnm148@gmail.com
     * @Param : accessToken
     */
    @Transactional
    public String getUserEmail(String token) {
        log.info("getUserEmail: {}", token);
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
        String[] emailParts = decodedJWT.getClaim("email").asString().split(NEW_EMAIL_CODE);
        return emailParts[0] + EMAIL_CODE + emailParts[1];
    }
}

package com.myapp.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.back.auth.jwt.refreshToken.RefreshToken;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private Long id;

    private String email;
    private String password;
    private LocalDateTime createTime;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    @JoinColumn(name = "refresh_token_id")
    @ToString.Exclude
    private RefreshToken jwtRefreshToken;

    public void setJwtRefreshToken(RefreshToken jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }

    public void createRefreshToken(RefreshToken refreshToken) {
        this.jwtRefreshToken = refreshToken;
    }
    public void updateRefreshToken(RefreshToken refreshToken) {
        this.jwtRefreshToken = refreshToken;
    }
    public void setRefreshToken(String refreshToken) { this.jwtRefreshToken.setRefreshToken(refreshToken); }
}

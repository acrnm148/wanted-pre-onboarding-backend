package com.myapp.back.controller;

import com.myapp.back.dto.request.BoardRequestDto;
import com.myapp.back.dto.response.BoardResponseDto;
import com.myapp.back.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RestBoardController", description = "게시판 API")
@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class RestBoardController {

    private final BoardService boardService;

    /**
     * 과제 3. 새로운 게시글을 생성하는 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody BoardRequestDto requestDto, @RequestHeader("Authorization") String token) {
        log.info("createPost: {}, {}", requestDto, token);
        if (token == null || token.length() < 7) {
            return ResponseEntity.badRequest().body("token is wrong!");
        }
        if (requestDto == null) {
            return ResponseEntity.badRequest().body("request is wrong!");
        }
        if (!boardService.createPost(requestDto, token.substring(7))) {
            return ResponseEntity.badRequest().body("failed to create post!");
        }
        return ResponseEntity.ok().body("success to create post!");
    }

    /**
     * 과제 4. 게시글 목록을 조회하는 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getAllPosts() {
        log.info("getAllPosts");
        List<BoardResponseDto> list = boardService.getAllPosts();
        if (list == null) {
            log.error("there are no posts!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok().body(list);
    }

    /**
     * 과제 5. 특정 게시글을 조회하는 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getPost(@PathVariable("boardId") Long boardId) {
        log.info("getPost: {}", boardId);
        BoardResponseDto responseDto = boardService.getPost(boardId);
        if (responseDto == null) {
            log.error("post does not exist!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 과제 6. 특정 게시글을 수정하는 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @PutMapping
    public ResponseEntity<String> updatePost(@RequestBody BoardRequestDto requestDto,
                                                       @RequestHeader("Authorization") String token) {
        log.info("updatePost: {}", requestDto);
        if (token == null || token.length() < 7) {
            return ResponseEntity.badRequest().body("token is wrong!");
        }
        if (requestDto == null) {
            return ResponseEntity.badRequest().body("request is wrong!");
        }
        if (!boardService.updatePost(requestDto, token.substring(7))) {
            return ResponseEntity.badRequest().body("failed to update post!");
        }
        return ResponseEntity.ok().body("success to update post!");
    }

    /**
     * 과제 7. 특정 게시글을 삭제하는 엔드포인트
     * @Author : acrnm148@gmail.com
     * @Param : LoginRequestDto.java
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deletePost(@PathVariable("boardId") Long boardId, @RequestHeader("Authorization") String token) {
        log.info("deletePost: {}, {}", boardId, token);
        if (token == null || token.length() < 7) {
            return ResponseEntity.badRequest().body("token is wrong!");
        }
        if (!boardService.deletePost(boardId, token.substring(7))) {
            return ResponseEntity.badRequest().body("failed to delete post!");
        }
        return ResponseEntity.ok().body("success to delete post!");
    }
}

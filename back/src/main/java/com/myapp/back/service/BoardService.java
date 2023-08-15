package com.myapp.back.service;

import com.myapp.back.dto.request.BoardRequestDto;
import com.myapp.back.dto.response.BoardResponseDto;
import com.myapp.back.model.Board;
import com.myapp.back.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserService userService;

    /**
     * 게시글 생성
     * @Author : acrnm148@gmail.com
     * @Param : BoardRequestDto.java
     * @Param : token
     * @Description : 조건이 있을 경우 false 반환, 추가 조건 없으므로 전부 true 반환
     */
    @Transactional
    public boolean createPost(BoardRequestDto requestDto, String token) {
        log.info("createPost: {}", requestDto);
        boardRepository.save(
                Board.builder()
                        .title(requestDto.getTitle())
                        .contents(requestDto.getContents())
                        .author(userService.getUserEmail(token))
                        .createTime(now())
                        .build()
        );
        return true;
    }

    /**
     * 게시글 목록 조회
     * @Author : acrnm148@gmail.com
     * @Param : Pageable
     */
    @Transactional
    public List<BoardResponseDto> getAllPosts(Pageable pageable) {
        log.info("getAllPosts");
        Page<Board> list = boardRepository.findAll(pageable);
        List<BoardResponseDto> responseList = new ArrayList<>();
        for (Board board : list) {
            BoardResponseDto response = new BoardResponseDto(board);
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * 특정 게시글 조회
     * @Author : acrnm148@gmail.com
     * @Param : boardId
     */
    @Transactional
    public BoardResponseDto getPost(Long boardId) {
        log.info("getPost: {}", boardId);
        Optional<Board> result = boardRepository.findById(boardId);
        if (!result.isPresent()) {
            return null;
        }
        Board board = result.get();
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .author(board.getAuthor())
                .createTime(board.getCreateTime())
                .build();
    }

    /**
     * 게시글 수정
     * @Author : acrnm148@gmail.com
     * @Param : BoardRequestDto.java
     * @Param : token
     */
    @Transactional
    public boolean updatePost(BoardRequestDto requestDto, String token) {
        log.info("updatePost: {}", requestDto);
        if (!validateAuthor(token, requestDto.getAuthor())) {
            return false;
        }
        boardRepository.save(
                Board.builder()
                        .id(requestDto.getId())
                        .title(requestDto.getTitle())
                        .contents(requestDto.getContents())
                        .author(userService.getUserEmail(token))
                        .createTime(now())
                        .build());
        return true;
    }

    /**
     * 특정 게시글 삭제
     * @Author : acrnm148@gmail.com
     * @Param : boardId
     * @Param : token
     */
    @Transactional
    public boolean deletePost(Long boardId, String token) {
        log.info("deletePost: {}", boardId);
        Optional<Board> result = boardRepository.findById(boardId);
        if (!result.isPresent()) {
            return false;
        }
        if (!validateAuthor(token, result.get().getAuthor())) {
            return false;
        }
        boardRepository.deleteById(boardId);
        return true;
    }

    /**
     * 작성자 일치 여부 체크
     * @Author : acrnm148@gmail.com
     * @Param : token
     * @Param : author
     */
    private boolean validateAuthor(String token, String author) {
        if (!userService.getUserEmail(token).equals(author)) {
            return false;
        }
        return true;
    }
}

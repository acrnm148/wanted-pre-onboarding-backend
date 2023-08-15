package com.myapp.back.service;

import com.myapp.back.dto.request.BoardRequestDto;
import com.myapp.back.dto.response.BoardResponseDto;
import com.myapp.back.model.Board;
import com.myapp.back.repository.BoardRepository;
import com.myapp.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        // 조건이 있을 경우 false 반환, 추가 조건 없으므로 전부 true 반환
        return true;
    }

    @Transactional
    public List<BoardResponseDto> getAllPosts() {
        log.info("getAllPosts");
        List<Board> list = boardRepository.findAll();
        List<BoardResponseDto> responseList = new ArrayList<>();
        for (Board board : list) {
            BoardResponseDto response = new BoardResponseDto(board);
            responseList.add(response);
        }
        return responseList;
    }

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

    private boolean validateAuthor(String token, String author) {
        System.out.println("byTokenEmail:"+userService.getUserEmail(token));
        System.out.println("author:"+author);
        if (!userService.getUserEmail(token).equals(author)) {
            return false;
        }
        return true;
    }
}

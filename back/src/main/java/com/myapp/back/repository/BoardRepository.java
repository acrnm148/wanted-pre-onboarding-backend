package com.myapp.back.repository;

import com.myapp.back.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    public Optional<Board> findById(Long boardId);
    public Page<Board> findAll(Pageable pageable);
    public void deleteById(Long boardId);
}

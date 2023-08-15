package com.myapp.back.repository;

import com.myapp.back.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    public Optional<Board> findById(Long boardId);
    public List<Board> findAll();
    public void deleteById(Long boardId);
}

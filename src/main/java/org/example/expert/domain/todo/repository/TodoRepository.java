package org.example.expert.domain.todo.repository;

import java.time.LocalDate;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryQuery {

    @Query("SELECT t FROM Todo t " +
        "LEFT JOIN FETCH t.user " +
        "WHERE (:weather IS NULL OR t.weather LIKE %:weather%)" +
        "AND (:startDay IS NULL OR DATE_FORMAT(t.modifiedAt, '%Y-%m-%d') >= :startDay)" +
        "AND (:endDay IS NULL OR DATE_FORMAT(t.modifiedAt, '%Y-%m-%d') <= :endDay)" +
        "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByWeatherAndModifiedAt(String weather, LocalDate startDay, LocalDate endDay, Pageable pageable);
}
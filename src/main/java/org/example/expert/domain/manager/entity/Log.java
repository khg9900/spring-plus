package org.example.expert.domain.manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long todoId;

    private Long managerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String message;

    private LocalDateTime accessTime;

    public Log(Long userId, Long todoId, Long managerId, String status, String message) {
        this.userId = userId;
        this.todoId = todoId;
        this.managerId = managerId;
        this.status = Status.valueOf(status);
        this.message = message;
        this.accessTime = LocalDateTime.now();
    }

    public enum Status {
        SUCCESS, FAILURE
    }
}

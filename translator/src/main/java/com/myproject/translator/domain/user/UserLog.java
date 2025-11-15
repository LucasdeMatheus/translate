package com.myproject.translator.domain.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private LogType type;

    private String action;

    @Column(columnDefinition = "TEXT")
    private String details;

    public UserLog(LogDTO logDTO) {
        this.type = logDTO.logType();
        this.action = logDTO.action();
        this.details = logDTO.details();
    }

    public UserLog() {
    }

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

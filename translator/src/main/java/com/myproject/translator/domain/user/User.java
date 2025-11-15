package com.myproject.translator.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String chatbotData;


    private LocalDate createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserLog> logs = new ArrayList<>();

    public User(UserDTO userDTO) {
        this.name = userDTO.name();
        this.email = userDTO.email();
    }

    public User() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    // Método útil para adicionar logs
    public void addLog(UserLog log) {
        log.setUser(this);
        logs.add(log);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserLog> getLogs() {
        return logs;
    }

    public void setLogs(List<UserLog> logs) {
        this.logs = logs;
    }

    public String getChatbotData() {
        return chatbotData;
    }

    public void setChatbotData(String chatbotData) {
        this.chatbotData = chatbotData;
    }

    public Long getId() {
        return id;
    }
}

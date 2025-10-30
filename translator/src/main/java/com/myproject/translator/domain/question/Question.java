package com.myproject.translator.domain.question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myproject.translator.domain.translation.Translation;
import jakarta.persistence.*;
import java.util.List;


@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ElementCollection
    private List<String> alternatives;

    private int correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "translation_id")
    @JsonIgnore
    private Translation translation;

    public Question() {
    }

    public Question(QuestionDTO dto, Translation translation) {
        this.description = dto.description();
        this.alternatives = dto.alternatives();
        this.correctAnswer = dto.correctAnswer();
        this.explanation = dto.explanation();
        this.translation = translation;
    }


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}

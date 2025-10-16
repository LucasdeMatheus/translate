package com.myproject.translator.domain.translation;

import com.myproject.translator.domain.question.Question;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data; // data da tradução
    private String text; // texto original
    private String literalTranslation; // tradução literal
    private String naturalTranslation; // tradução natural

    @Column(columnDefinition = "TEXT")
    private String explanation; // explicação da tradução

    @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> listOfQuestions;

    public Translation(TranslationDTO translationDTO) {
        this.data = LocalDate.now();
        this.text = translationDTO.expression();
        this.literalTranslation = translationDTO.literalTranslation();
        this.naturalTranslation = translationDTO.naturalTranslation();
        this.explanation = translationDTO.explanation();

        if (translationDTO.questions() != null) {
            this.listOfQuestions = translationDTO.questions().stream()
                    .map(q -> new Question(q, this))
                    .toList();
        }
    }

    public Translation() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLiteralTranslation() {
        return literalTranslation;
    }

    public void setLiteralTranslation(String literalTranslation) {
        this.literalTranslation = literalTranslation;
    }

    public String getNaturalTranslation() {
        return naturalTranslation;
    }

    public void setNaturalTranslation(String naturalTranslation) {
        this.naturalTranslation = naturalTranslation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<Question> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(List<Question> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }
}

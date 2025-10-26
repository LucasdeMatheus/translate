package com.myproject.translator.domain.dialogue;

import com.myproject.translator.domain.util.Language;
import jakarta.persistence.*;


@Entity
public class Dialogue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL) // mapeia para inteiro
    private Language language;

    @Column(columnDefinition = "TEXT")
    private String explanation;


    private String translationAnswer;
    private String answer;

    private String translationAsk;
    private String ask;

    private String context;


    public Long getId() {
        return id;
    }



    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }


    public String getTranslationAnswer() {
        return translationAnswer;
    }

    public void setTranslationAnswer(String translationAnswer) {
        this.translationAnswer = translationAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String ansewer) {
        this.answer = ansewer;
    }

    public String getTranslationAsk() {
        return translationAsk;
    }

    public void setTranslationAsk(String translationAsk) {
        this.translationAsk = translationAsk;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String context) {
        this.ask = context;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

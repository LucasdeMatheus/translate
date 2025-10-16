package com.myproject.translator.domain.question;

import java.util.List;

public record QuestionDTO(
        String description,
        List<String> alternatives,
        int correctAnswer,
        String explanation
) {}

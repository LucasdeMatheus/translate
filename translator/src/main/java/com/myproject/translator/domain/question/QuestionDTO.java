package com.myproject.translator.domain.question;

import java.util.List;

public record QuestionDTO(
        Long questionId,
        Long tranlationId,
        String description,
        List<String> alternatives,
        int correctAnswer,
        String explanation
) {}

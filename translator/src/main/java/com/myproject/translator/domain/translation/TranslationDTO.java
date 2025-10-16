package com.myproject.translator.domain.translation;

import com.myproject.translator.domain.question.QuestionDTO;

import java.util.List;

public record TranslationDTO(
        String expression,
        List<QuestionDTO> questions,
        String literalTranslation,
        String naturalTranslation,
        String explanation
) {}

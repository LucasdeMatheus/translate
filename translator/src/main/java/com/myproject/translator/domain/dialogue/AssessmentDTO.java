package com.myproject.translator.domain.phrase;

public record AssessmentDTO(
        boolean GoodAnswer,
        String feedback,
        byte[] audioFeedback
) {
}

package com.myproject.translator.domain.dialogue;

public record AssessmentDTO(
        boolean GoodAnswer,
        String feedback,
        byte[] audioFeedback
) {
}

package com.myproject.translator.domain.translation;

import jakarta.validation.constraints.NotBlank;

public record TranslateDTO(
        @NotBlank
        String expression,
        @NotBlank
        Language LANGUAGE
) {
}

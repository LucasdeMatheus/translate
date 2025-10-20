package com.myproject.translator.domain.translation;

import com.myproject.translator.domain.util.Language;
import jakarta.validation.constraints.NotBlank;

public record TranslateDTO(
        @NotBlank
        String expression,
        @NotBlank
        Language LANGUAGE
) {
}

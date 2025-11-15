package com.myproject.translator.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogDTO(
        @NotNull
        LogType logType,
        @NotBlank
        String action,
        @NotBlank
        String details
) {
}

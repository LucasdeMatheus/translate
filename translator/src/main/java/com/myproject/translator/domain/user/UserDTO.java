package com.myproject.translator.domain.user;

public record UserDTO (
        Long id,
        String email,
        String name
) {
}

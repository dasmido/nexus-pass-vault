package dev.dasmido.nexus.rest;

import jakarta.validation.constraints.NotBlank;

public record CreatePasswordRequest(
        @NotBlank(message = "site is required") String site,
        @NotBlank(message = "username is required") String username,
        @NotBlank(message = "secret is required") String secret
) {
}


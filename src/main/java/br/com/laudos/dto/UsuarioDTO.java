package br.com.laudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        Long id,
        @NotNull @NotBlank String username,
        String email,
        @NotNull @NotBlank String role
) {
}

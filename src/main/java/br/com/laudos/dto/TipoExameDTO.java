package br.com.laudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record TipoExameDTO(
        Integer id,
        @NotNull @NotBlank @Length(max = 150) String descricao,
        @NotNull @NotBlank String ordena,
        boolean esofago,
        boolean estomago,
        boolean duodeno,
        boolean intestino,
        boolean pancreas
) {
}

package br.com.laudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record DrogaDTO(
        Integer id,
        @NotNull @NotBlank @Length(min = 5, max = 100) String nomedroga
) {
}

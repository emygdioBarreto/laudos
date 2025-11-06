package br.com.laudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LocalDTO(
        Integer id,
        @NotNull @NotBlank @Length(min = 4, max = 140) String descricao
) {
}

package br.com.laudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record EquipamentoDTO(
        Integer id,
        @NotNull @NotBlank @Length(min = 5, max = 140) String descricao,
        String superior,
        String inferior,
        String direita,
        String esquerda,
        String modLaudo,
        String cidade,
        String ordena
) {
}

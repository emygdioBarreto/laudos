package br.com.laudos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LaudoCreateDTO(
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull LocalDate dataCriacao,
        @NotNull Long equipamentoId,
        @NotBlank String paciente,
        String idade,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull LocalDate nascimento,
        @NotNull String sexo,
        @NotNull Long solicitanteId,
        @NotNull Long procedenciaId,
        @NotNull Long premedicacaoId,
        @NotNull Long localExameId,
        @NotNull String medicoExecutorCrm,
        @NotNull Long resumoId,
        String observacaoClinica,
        String esofago,
        String estomago,
        String duodeno,
        String intestino,
        String pancreas,
        String solucao,
        String conclusao,
        String observacao,
        @NotNull Long tipoExameId
) {
}


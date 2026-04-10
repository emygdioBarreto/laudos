package br.com.laudos.dto;

public record LaudoIdsDTO(
        Long equipamentoId,
        Long solicitanteId,
        Long procedenciaId,
        Long premedicacaoId,
        Long localExameId,
        Long tipoExameId,
        String medicoExecutorCrm,
        Long resumoId
) {}
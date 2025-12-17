package br.com.laudos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public record LaudoUpdateDTO(
        Long id,
        @JsonProperty("datalaudo")
        @JsonFormat(pattern = "yyyy-MM-dd")
        String data,
        Integer equipamentoId,
        String paciente,
        String idade,
        @JsonFormat(pattern = "yyyy-MM-dd")
        String nascimento,
        String sexo,
        Integer solicitanteId,
        Integer procedenciaId,
        Integer premedicacaoId,
        Integer localExameId,
        String medicoExecutorCrm,
        Integer resumoId,
        String observacaoClinica,
        @Column(name = "esofago", columnDefinition = "TEXT") String esofago,
        @Column(name = "estomago", columnDefinition = "TEXT") String estomago,
        @Column(name = "duodeno", columnDefinition = "TEXT") String duodeno,
        @Column(name = "intestino", columnDefinition = "TEXT") String intestino,
        @Column(name = "pancreas", columnDefinition = "TEXT") String pancreas,
        String solucao,
        String conclusao,
        String observacao,
        Integer tipoExameId) {
}

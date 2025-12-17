package br.com.laudos.dto;

import br.com.laudos.domain.*;
import jakarta.persistence.Column;

public record LaudoDTO(
    Long id,
    String data,
    Equipamento equipamento,
    String paciente,
    String idade,
    String nascimento,
    String sexo,
    Solicitante solicitante,
    Procedencia procedencia,
    Premedicacao premedicacao,
    Local localExame,
    Medico medicoExecutor,
    Resumo resumo,
    String observacaoClinica,
    @Column(name = "esofago", columnDefinition = "TEXT") String esofago,
    @Column(name = "estomago", columnDefinition = "TEXT") String estomago,
    @Column(name = "duodeno", columnDefinition = "TEXT") String duodeno,
    @Column(name = "intestino", columnDefinition = "TEXT") String intestino,
    @Column(name = "pancreas", columnDefinition = "TEXT") String pancreas,
    String solucao,
    String conclusao,
    String observacao,
    TipoExame tipoExame) {
}

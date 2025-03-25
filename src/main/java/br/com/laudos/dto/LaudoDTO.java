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
    String esofago,
    String estomago,
    String duodeno,
    String intestino,
    String pancreas,
    String solucao,
    String conclusao,
    @Column(name = "observacao", columnDefinition = "TEXT") String observacao,
    TipoExame tipoExame) {
}

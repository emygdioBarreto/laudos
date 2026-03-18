package br.com.laudos.dto;

import java.time.LocalDate;

public record LprintDTO(
        Integer laudoId,
        LocalDate data,
        String equipamento,
        String paciente,
        String idade,
        LocalDate nascimento,
        String sexo,
        String solicitante,
        String procedenciaDescricao,
        String premedicacaoDescricao,
        String localexameDescricao,
        String medicoExecutor,
        String resumo,
        String esofago,
        String estomago,
        String duodeno,
        String intestinogrosso,
        String pancreas,
        String solucao,
        String conclusao,
        String obs,
        String obsclin,
        String crm,
        String tipoExameDescricao
) {
}

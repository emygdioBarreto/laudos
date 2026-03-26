package br.com.laudos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LaudoDTO {

    Long idLaudo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    LocalDate dataCriacao;
    // Objeto Equipamento
    @NotNull Long equipamentoId;
    @NotNull String equipamentoDescricao;

    @NotNull String paciente;
    String idade;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull LocalDate nascimento;
    @NotNull String sexo;
    // Objeto Solicitante
    @NotNull Long solicitanteId;
    @NotNull String solicitanteNome;

    // Objeto Procedencia
    @NotNull Long procedenciaId;
    @NotNull String procedenciaDescricao;

    // Objeto Premedicacao
    @NotNull Long premedicacaoId;
    @NotNull String premedicacaoDescricao;

    // Objeto Local
    @NotNull Long localExameId;
    @NotNull String localExameDescricao;

    // Objeto Medico
    @NotNull String medicoCrm;
    @NotNull String medicoExecutor;

    // Objeto Resumo
    @NotNull Long resumoId;
    @NotNull String resumoDescricao;

    String observacaoClinica;
    String esofago;
    String estomago;
    String duodeno;
    String intestino;
    String pancreas;
    String solucao;
    String conclusao;
    String observacao;

    // Objeto TipoExame
    @NotNull Long tipoExameId;
    @NotNull String tipoExameDescricao;

    // Hash de Validação
    String hashValidacao;
}


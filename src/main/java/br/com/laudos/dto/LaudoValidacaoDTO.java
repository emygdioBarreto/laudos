package br.com.laudos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaudoValidacaoDTO {
    private String paciente;
    private LocalDate dataExame;
    private String tipoExame;
    private String medico;
    private String crm;
    private String status;
    private String hash;
}
package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Procedencia;
import br.com.laudos.dto.ProcedenciaDTO;
import org.springframework.stereotype.Component;

@Component
public class ProcedenciaMapper {

    public ProcedenciaDTO toDTO(Procedencia procedencia) {
        if (procedencia == null) {
            return null;
        }
        return new ProcedenciaDTO(procedencia.getId(), procedencia.getDescricao());
    }

    public Procedencia toEntity(ProcedenciaDTO procedenciaDTO) {
        if (procedenciaDTO == null) {
            return null;
        }
        Procedencia procedencia = new Procedencia();
        if (procedenciaDTO.id() != null) {
            procedencia.setId(procedenciaDTO.id());
        }
        procedencia.setDescricao(procedenciaDTO.descricao());
        return procedencia;
    }
}

package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Solicitante;
import br.com.laudos.dto.SolicitanteDTO;
import org.springframework.stereotype.Component;

@Component
public class SolicitanteMapper {

    public SolicitanteDTO toDTO(Solicitante solicitante) {
        if (solicitante == null) {
            return null;
        }
        return new SolicitanteDTO(solicitante.getId(), solicitante.getMedicoSolicitante());
    }

    public Solicitante toEntity(SolicitanteDTO solicitanteDTO) {
        if (solicitanteDTO == null) {
            return null;
        }
        Solicitante solicitante = new Solicitante();
        if (solicitanteDTO.id() != null) {
            solicitante.setId(solicitanteDTO.id());
        }
        solicitante.setMedicoSolicitante(solicitanteDTO.medicoSolicitante());
        return solicitante;
    }
}

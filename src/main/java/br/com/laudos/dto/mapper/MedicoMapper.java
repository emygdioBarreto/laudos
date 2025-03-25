package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Medico;
import br.com.laudos.dto.MedicoDTO;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

    public MedicoDTO toDTO(Medico medico) {
        if (medico == null) {
            return null;
        }
        return new MedicoDTO(medico.getCrm(), medico.getMedicoExecutor());
    }

    public Medico toEntity(MedicoDTO medicoDTO) {
        if (medicoDTO == null) {
            return null;
        }
        Medico medico = new Medico();
        if (medicoDTO.crm() != null) {
            medico.setCrm(medicoDTO.crm());
        }
        medico.setMedicoExecutor(medicoDTO.medicoExecutor());
        return medico;
    }
}

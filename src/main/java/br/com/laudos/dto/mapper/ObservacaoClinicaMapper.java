package br.com.laudos.dto.mapper;

import br.com.laudos.domain.ObservacaoClinica;
import br.com.laudos.dto.ObservacaoClinicaDTO;
import org.springframework.stereotype.Component;

@Component
public class ObservacaoClinicaMapper {

    public ObservacaoClinicaDTO toDTO(ObservacaoClinica observacaoClinica) {
        if (observacaoClinica == null) {
            return null;
        }
        return new ObservacaoClinicaDTO(observacaoClinica.getId(), observacaoClinica.getDescricao());
    }

    public ObservacaoClinica toEntity(ObservacaoClinicaDTO observacaoClinicaDTO) {
        if (observacaoClinicaDTO == null) {
            return null;
        }
        ObservacaoClinica observacaoClinica = new ObservacaoClinica();
        if (observacaoClinicaDTO.id() != null) {
            observacaoClinica.setId(observacaoClinicaDTO.id());
        }
        observacaoClinica.setDescricao(observacaoClinicaDTO.descricao());
        return observacaoClinica;
    }
}

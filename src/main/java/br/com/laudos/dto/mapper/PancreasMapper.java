package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Pancreas;
import br.com.laudos.dto.PancreasDTO;
import org.springframework.stereotype.Component;

@Component
public class PancreasMapper {

    public PancreasDTO toDTO(Pancreas pancreas) {
        if (pancreas == null) {
            return null;
        }
        return new PancreasDTO(pancreas.getId(), pancreas.getDescricao());
    }

    public Pancreas toEntity(PancreasDTO pancreasDTO) {
        if (pancreasDTO == null) {
            return null;
        }
        Pancreas pancreas = new Pancreas();
        if (pancreasDTO.id() != null && pancreasDTO.id() > 0) {
            pancreas.setId(pancreasDTO.id());
        }
        pancreas.setDescricao(pancreasDTO.descricao());
        return pancreas;
    }
}

package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Pancrea;
import br.com.laudos.dto.PancreaDTO;
import org.springframework.stereotype.Component;

@Component
public class PancreaMapper {

    public PancreaDTO toDTO(Pancrea pancrea) {
        if (pancrea == null) {
            return null;
        }
        return new PancreaDTO(pancrea.getId(), pancrea.getDescricao());
    }

    public Pancrea toEntity(PancreaDTO pancreaDTO) {
        if (pancreaDTO == null) {
            return null;
        }
        Pancrea pancrea = new Pancrea();
        if (pancreaDTO.id() != null && pancreaDTO.id() > 0) {
            pancrea.setId(pancreaDTO.id());
        }
        pancrea.setDescricao(pancreaDTO.descricao());
        return pancrea;
    }
}

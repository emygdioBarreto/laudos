package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Intestino;
import br.com.laudos.dto.IntestinoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class IntestinoMapper {

    public IntestinoDTO toDTO(Intestino intestino) {
        if (intestino == null) {
            return null;
        }
        return new IntestinoDTO(intestino.getId(), intestino.getDescricao());
    }

    public Intestino toEntity(@Valid IntestinoDTO intestinoDTO) {
        if (intestinoDTO == null) {
            return null;
        }
        Intestino intestino = new Intestino();
        if (intestinoDTO.id() != null) {
            intestino.setId(intestinoDTO.id());
        }
        intestino.setDescricao(intestinoDTO.descricao());
        return intestino;
    }
}

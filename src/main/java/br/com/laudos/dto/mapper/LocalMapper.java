package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Local;
import br.com.laudos.dto.LocalDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class LocalMapper {

    public LocalDTO toDTO(Local local) {
        if (local == null) {
            return null;
        }
        return new LocalDTO(local.getId(), local.getDescricao());
    }

    public Local toEntity(@Valid LocalDTO localDTO) {
        if (localDTO == null) {
            return null;
        }
        Local local = new Local();
        if (localDTO.id() != null) {
            local.setId(localDTO.id());
        }
        local.setDescricao(localDTO.descricao());
        return local;
    }
}

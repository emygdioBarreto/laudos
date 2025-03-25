package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Duodeno;
import br.com.laudos.dto.DuodenoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class DuodenoMapper {

    public DuodenoDTO toDTO(Duodeno duodeno) {
        if (duodeno == null) {
            return null;
        }
        return new DuodenoDTO(duodeno.getId(), duodeno.getDescricao());
    }

    public Duodeno toEntity(@Valid DuodenoDTO duodenoDTO) {
        if (duodenoDTO == null) {
            return null;
        }

        Duodeno duodeno = new Duodeno();
        if (duodenoDTO.id() != null) {
            duodeno.setId(duodenoDTO.id());
        }
        duodeno.setDescricao(duodenoDTO.descricao());
        return duodeno;
    }
}

package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Droga;
import br.com.laudos.dto.DrogaDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class DrogaMapper {

    public DrogaDTO toDTO(Droga droga) {
        if (droga == null) {
            return null;
        }
        return new DrogaDTO(droga.getId(), droga.getNomedroga());
    }

    public Droga toEntity(@Valid DrogaDTO drogaDTO) {
        if (drogaDTO == null) {
            return null;
        }

        Droga droga = new Droga();
        if (drogaDTO.id() != null) {
            droga.setId(drogaDTO.id());
        }
        droga.setNomedroga(drogaDTO.nomedroga());
        return droga;
    }
}

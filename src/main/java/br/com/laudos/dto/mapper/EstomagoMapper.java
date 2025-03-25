package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Estomago;
import br.com.laudos.dto.EstomagoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class EstomagoMapper {

    public EstomagoDTO toDTO(Estomago estomago) {
        if (estomago == null) {
            return null;
        }
        return new EstomagoDTO(estomago.getId(), estomago.getDescricao());
    }

    public Estomago toEntity(@Valid EstomagoDTO estomagoDTO) {
        if (estomagoDTO == null) {
            return null;
        }
        Estomago estomago = new Estomago();
        if (estomagoDTO.id() != null) {
            estomago.setId(estomagoDTO.id());
        }
        estomago.setDescricao(estomagoDTO.descricao());
        return estomago;
    }
}

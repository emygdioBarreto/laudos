package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Concluir;
import br.com.laudos.dto.ConcluirDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class ConcluirMapper {

    public ConcluirDTO toDTO(Concluir concluir) {
        if (concluir == null) {
            return null;
        }
        return new ConcluirDTO(concluir.getId(), concluir.getConclusao());
    }

    public Concluir toEntity(@Valid ConcluirDTO concluirDTO) {
        if (concluirDTO == null) {
            return null;
        }
        Concluir concluir = new Concluir();
        if (concluirDTO.id() != null) {
            concluir.setId(concluirDTO.id());
        }
        concluir.setConclusao(concluirDTO.conclusao());
        return concluir;
    }
}

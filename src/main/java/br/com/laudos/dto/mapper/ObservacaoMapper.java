package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Observacao;
import br.com.laudos.dto.ObservacaoDTO;
import org.springframework.stereotype.Component;

@Component
public class ObservacaoMapper {

    public ObservacaoDTO toDTO(Observacao observacao) {
        if (observacao == null) {
            return null;
        }
        return new ObservacaoDTO(observacao.getId(), observacao.getDescricao());
    }

    public Observacao toEntity(ObservacaoDTO observacaoDTO) {
        if (observacaoDTO == null) {
            return null;
        }
        Observacao observacao = new Observacao();
        if (observacao.getId() != null) {
            observacao.setId(observacaoDTO.id());
        }
        observacao.setDescricao(observacaoDTO.descricao());
        return observacao;
    }
}

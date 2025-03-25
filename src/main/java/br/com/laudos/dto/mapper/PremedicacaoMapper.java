package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Premedicacao;
import br.com.laudos.dto.PremedicacaoDTO;
import org.springframework.stereotype.Component;

@Component
public class PremedicacaoMapper {
    
    public PremedicacaoDTO toDTO(Premedicacao premedicacao) {
        if (premedicacao == null) {
            return null;
        }
        return new PremedicacaoDTO(premedicacao.getId(), premedicacao.getAnalgesia());
    }
    
    public Premedicacao toEntity(PremedicacaoDTO premedicacaoDTO) {
        if (premedicacaoDTO == null) {
            return null;
        }
        Premedicacao premedicacao = new Premedicacao();
        if (premedicacaoDTO.id() != null) {
            premedicacao.setId(premedicacaoDTO.id());
        }
        premedicacao.setAnalgesia(premedicacaoDTO.analgesia());
        return premedicacao;
    }
}

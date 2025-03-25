package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Resumo;
import br.com.laudos.dto.ResumoDTO;
import org.springframework.stereotype.Component;

@Component
public class ResumoMapper {

    public ResumoDTO toDTO(Resumo resumo) {
        if (resumo == null) {
            return null;
        }
        return new ResumoDTO(resumo.getId(), resumo.getDescricao());
    }

    public Resumo toEntity(ResumoDTO resumoDTO) {
        if (resumoDTO == null) {
            return null;
        }
        Resumo resumo = new Resumo();
        if (resumoDTO.id() != null) {
            resumo.setId(resumoDTO.id());
        }
        resumo.setDescricao(resumoDTO.descricao());
        return resumo;
    }
}

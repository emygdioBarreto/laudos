package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Equipamento;
import br.com.laudos.dto.EquipamentoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {

    public EquipamentoDTO toDTO(Equipamento equipamento) {
        if (equipamento == null) {
            return null;
        }
        return new EquipamentoDTO(equipamento.getId(),
                equipamento.getDescricao(),
                equipamento.getSuperior(),
                equipamento.getInferior(),
                equipamento.getDireita(),
                equipamento.getEsquerda(),
                equipamento.getModLaudo(),
                equipamento.getCidade(),
                equipamento.getOrdena());
    }

    public Equipamento toEntity(@Valid EquipamentoDTO equipamentoDTO) {
        if (equipamentoDTO == null) {
            return null;
        }
        Equipamento equipamento = new Equipamento();
        if (equipamentoDTO.id() != null){
            equipamento.setId(equipamento.getId());
        }
        equipamento.setDescricao(equipamentoDTO.descricao());
        equipamento.setSuperior(equipamentoDTO.superior());
        equipamento.setInferior(equipamentoDTO.inferior());
        equipamento.setDireita(equipamentoDTO.direita());
        equipamento.setEsquerda(equipamentoDTO.esquerda());
        equipamento.setModLaudo(equipamentoDTO.modLaudo());
        equipamento.setCidade(equipamentoDTO.cidade());
        equipamento.setOrdena(equipamentoDTO.ordena());
        return equipamento;
    }
}

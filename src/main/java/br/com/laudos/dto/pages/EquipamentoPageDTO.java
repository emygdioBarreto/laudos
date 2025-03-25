package br.com.laudos.dto.pages;

import br.com.laudos.dto.EquipamentoDTO;

import java.util.List;

public record EquipamentoPageDTO(
        List<EquipamentoDTO> equipamentos, int totalPages, long totalElements) {
}

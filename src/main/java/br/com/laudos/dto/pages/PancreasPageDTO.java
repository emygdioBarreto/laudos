package br.com.laudos.dto.pages;

import br.com.laudos.dto.PancreasDTO;

import java.util.List;

public record PancreasPageDTO(
        List<PancreasDTO> pancreass, int totalPages, long totalElements) {
}

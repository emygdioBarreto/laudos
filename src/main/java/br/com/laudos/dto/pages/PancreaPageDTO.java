package br.com.laudos.dto.pages;

import br.com.laudos.dto.PancreaDTO;

import java.util.List;

public record PancreaPageDTO(
        List<PancreaDTO> pancreas, int totalPages, long totalElements) {
}

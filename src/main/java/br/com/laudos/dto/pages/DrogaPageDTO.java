package br.com.laudos.dto.pages;

import br.com.laudos.dto.DrogaDTO;

import java.util.List;

public record DrogaPageDTO (
        List<DrogaDTO> drogas, int totalPages, long totalElements) {
}

package br.com.laudos.dto.pages;

import br.com.laudos.dto.EstomagoDTO;

import java.util.List;

public record EstomagoPageDTO(
        List<EstomagoDTO> estomagos, int totalPages, long totalElements) {
}

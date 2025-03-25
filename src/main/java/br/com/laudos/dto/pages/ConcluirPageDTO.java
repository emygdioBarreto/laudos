package br.com.laudos.dto.pages;

import br.com.laudos.dto.ConcluirDTO;

import java.util.List;

public record ConcluirPageDTO(
        List<ConcluirDTO> conclusoes, int totalPages, long totalElements) {
}

package br.com.laudos.dto.pages;

import br.com.laudos.dto.IntestinoDTO;

import java.util.List;

public record IntestinoPageDTO(
        List<IntestinoDTO> intestinos, int totalPages, long totalElements) {
}

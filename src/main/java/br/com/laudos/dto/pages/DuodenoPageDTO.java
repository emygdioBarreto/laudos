package br.com.laudos.dto.pages;

import br.com.laudos.dto.DuodenoDTO;

import java.util.List;

public record DuodenoPageDTO(
    List<DuodenoDTO> duodenos, int totalPages, long totalElements) {
}

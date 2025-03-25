package br.com.laudos.dto.pages;

import br.com.laudos.dto.LocalDTO;

import java.util.List;

public record LocalPageDTO(List<LocalDTO> locais, int totalPages, long totalElements) {
}

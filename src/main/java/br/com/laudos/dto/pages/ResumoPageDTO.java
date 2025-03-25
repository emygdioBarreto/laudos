package br.com.laudos.dto.pages;

import br.com.laudos.dto.ResumoDTO;

import java.util.List;

public record ResumoPageDTO(
        List<ResumoDTO> resumos, int totalPages, long totalElements) {
}

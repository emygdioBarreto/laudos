package br.com.laudos.dto.pages;

import br.com.laudos.dto.SolicitanteDTO;

import java.util.List;

public record SolicitantePageDTO(
        List<SolicitanteDTO> solicitantes, int totalPages, long totalElements) {
}

package br.com.laudos.dto.pages;

import br.com.laudos.dto.MedicoDTO;

import java.util.List;

public record MedicoPageDTO(
        List<MedicoDTO> medicos, int totalPages, long totalElements) {
}

package br.com.laudos.dto.pages;

import br.com.laudos.dto.ProcedenciaDTO;

import java.util.List;

public record ProcedenciaPageDTO(
        List<ProcedenciaDTO> procedencias, int totalPages, long totalElements) {
}

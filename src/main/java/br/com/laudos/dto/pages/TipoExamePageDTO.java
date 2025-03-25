package br.com.laudos.dto.pages;

import br.com.laudos.dto.TipoExameDTO;

import java.util.List;

public record TipoExamePageDTO(
        List<TipoExameDTO> tipoexames, int totalPages, long totalElements) {
}

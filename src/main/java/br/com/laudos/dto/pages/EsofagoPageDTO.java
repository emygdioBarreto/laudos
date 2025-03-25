package br.com.laudos.dto.pages;

import br.com.laudos.dto.EsofagoDTO;

import java.util.List;

public record EsofagoPageDTO(
        List<EsofagoDTO> esofagos, int totalPages, long totalElements) {
}

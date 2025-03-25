package br.com.laudos.dto.pages;

import br.com.laudos.dto.PremedicacaoDTO;

import java.util.List;

public record PremedicacaoPageDTO(
        List<PremedicacaoDTO> premedicacoes, int totalPages, long totalElements) {
}

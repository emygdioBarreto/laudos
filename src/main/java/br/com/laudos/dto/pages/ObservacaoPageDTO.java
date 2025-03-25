package br.com.laudos.dto.pages;

import br.com.laudos.dto.ObservacaoDTO;

import java.util.List;

public record ObservacaoPageDTO(
        List<ObservacaoDTO> observacoes, int totalPages, long totalElements) {
}

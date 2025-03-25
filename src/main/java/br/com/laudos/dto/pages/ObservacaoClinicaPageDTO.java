package br.com.laudos.dto.pages;

import br.com.laudos.dto.ObservacaoClinicaDTO;

import java.util.List;

public record ObservacaoClinicaPageDTO(
        List<ObservacaoClinicaDTO> observacoesClinicas, int totalPages, long totalElements) {
}

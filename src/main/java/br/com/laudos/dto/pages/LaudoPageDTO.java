package br.com.laudos.dto.pages;

import br.com.laudos.dto.LaudoDTO;
import java.util.List;

public record LaudoPageDTO(List<LaudoDTO> laudos, int totalPages, long totalElements) {
}

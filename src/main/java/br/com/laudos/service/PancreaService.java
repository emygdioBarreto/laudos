package br.com.laudos.service;

import br.com.laudos.domain.Pancrea;
import br.com.laudos.dto.PancreaDTO;
import br.com.laudos.dto.mapper.PancreaMapper;
import br.com.laudos.dto.pages.PancreaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.PancreaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PancreaService {

    private final PancreaRepository repository;
    private final PancreaMapper mapper;

    public PancreaDTO salvar(@Valid @NotNull PancreaDTO pancreasDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(pancreasDTO)));
    }

    public PancreaDTO update(@NotNull @Positive Integer id, @Valid @NotNull PancreaDTO pancreasDTO) {
        return repository.findById(id)
                .map(reg -> {
                    reg.setDescricao(pancreasDTO.descricao());
                    return mapper.toDTO(repository.save(reg));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(id)));
    }

    public PancreaPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Pancrea> pagePancreas = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<PancreaDTO> pancreas = pagePancreas.get().map(mapper::toDTO).toList();
        return new PancreaPageDTO(pancreas, pagePancreas.getTotalPages(), pagePancreas.getTotalElements());
    }

    public PancreaDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(()-> new RecordNotFoundException(id));
    }
}

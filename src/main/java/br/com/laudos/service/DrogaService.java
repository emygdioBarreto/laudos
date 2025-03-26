package br.com.laudos.service;

import br.com.laudos.domain.Droga;
import br.com.laudos.dto.DrogaDTO;
import br.com.laudos.dto.mapper.DrogaMapper;
import br.com.laudos.dto.pages.DrogaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.DrogaRepository;
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
public class DrogaService {

    private final DrogaRepository repository;
    private final DrogaMapper mapper;

    public DrogaDTO salvar(@Valid @NotNull DrogaDTO drogaDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(drogaDTO)));
    }

    public DrogaDTO update(@NotNull @Positive Integer id, @Valid @NotNull DrogaDTO drogaDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setNomedroga(drogaDTO.nomedroga());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public DrogaPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Droga> pageDroga = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<DrogaDTO> drogas = pageDroga.get().map(mapper::toDTO).toList();
        return new DrogaPageDTO(drogas, pageDroga.getTotalPages(), pageDroga.getTotalElements());
    }

    public DrogaDTO findById(@NotNull @Positive Integer id) {
        return this.repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(Long.valueOf(id)));
    }
}

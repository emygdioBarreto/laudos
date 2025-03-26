package br.com.laudos.service;

import br.com.laudos.domain.Pancreas;
import br.com.laudos.dto.PancreasDTO;
import br.com.laudos.dto.mapper.PancreasMapper;
import br.com.laudos.dto.pages.PancreasPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.PancreasRepository;
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
public class PancreasService {

    private final PancreasRepository repository;
    private final PancreasMapper mapper;

    public PancreasDTO salvar(@Valid @NotNull PancreasDTO pancreasDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(pancreasDTO)));
    }

    public PancreasDTO update(@NotNull @Positive Integer id, @Valid @NotNull PancreasDTO pancreasDTO) {
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

    public PancreasPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Pancreas> pagePancreas = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<PancreasDTO> pandreass = pagePancreas.get().map(mapper::toDTO).toList();
        return new PancreasPageDTO(pandreass, pagePancreas.getTotalPages(), pagePancreas.getTotalElements());
    }

    public PancreasDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(()-> new RecordNotFoundException(id));
    }
}

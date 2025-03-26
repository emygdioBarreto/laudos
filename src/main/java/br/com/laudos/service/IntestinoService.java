package br.com.laudos.service;

import br.com.laudos.domain.Intestino;
import br.com.laudos.dto.IntestinoDTO;
import br.com.laudos.dto.mapper.IntestinoMapper;
import br.com.laudos.dto.pages.IntestinoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.IntestinoRepository;
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
public class IntestinoService {

    private final IntestinoRepository repository;
    private final IntestinoMapper mapper;

    public IntestinoDTO salvar(@Valid @NotNull IntestinoDTO intestinoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(intestinoDTO)));
    }

    public IntestinoDTO update(@NotNull @Positive Integer id, @Valid @NotNull IntestinoDTO intestinoDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setDescricao(intestinoDTO.descricao());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public IntestinoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Intestino> pageIntestino = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<IntestinoDTO> intestinos = pageIntestino.get().map(mapper::toDTO).toList();
        return new IntestinoPageDTO(intestinos, pageIntestino.getTotalPages(), pageIntestino.getTotalElements());
    }

    public IntestinoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

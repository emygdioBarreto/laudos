package br.com.laudos.service;

import br.com.laudos.domain.Duodeno;
import br.com.laudos.dto.DuodenoDTO;
import br.com.laudos.dto.mapper.DuodenoMapper;
import br.com.laudos.dto.pages.DuodenoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.DuodenoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DuodenoService {

    private final DuodenoRepository repository;
    private final DuodenoMapper mapper;

    public DuodenoService(DuodenoRepository repository, DuodenoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public DuodenoDTO salvar(@Valid @NotNull DuodenoDTO duodenoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(duodenoDTO)));
    }

    public DuodenoDTO update(@NotNull @Positive Integer id, @Valid @NotNull DuodenoDTO duodenoDTO) {
        return repository.findById(id)
                .map(duodeno -> {
                    duodeno.setDescricao(duodenoDTO.descricao());
                    return mapper.toDTO(repository.save(duodeno));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public DuodenoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Duodeno> pageDuodeno = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<DuodenoDTO> duodenos = pageDuodeno.get().map(mapper::toDTO).toList();
        return new DuodenoPageDTO(duodenos, pageDuodeno.getTotalPages(), pageDuodeno.getTotalElements());
    }

    public DuodenoDTO findById(@NotNull @Positive Integer id) {
        return this.repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(Long.valueOf(id)));
    }
}

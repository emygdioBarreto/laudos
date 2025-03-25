package br.com.laudos.service;

import br.com.laudos.domain.Estomago;
import br.com.laudos.dto.EstomagoDTO;
import br.com.laudos.dto.mapper.EstomagoMapper;
import br.com.laudos.dto.pages.EstomagoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.EstomagoRepository;
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
public class EstomagoService {

    private final EstomagoRepository repository;
    private final EstomagoMapper mapper;

    public EstomagoService(EstomagoRepository repository, EstomagoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EstomagoDTO salvar(@Valid @NotNull EstomagoDTO estomagoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(estomagoDTO)));
    }

    public EstomagoDTO update(@NotNull @Positive Integer id, @Valid @NotNull EstomagoDTO estomagoDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setDescricao(estomagoDTO.descricao());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public EstomagoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Estomago> pageEstomago = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<EstomagoDTO> estomagos = pageEstomago.get().map(mapper::toDTO).toList();
        return new EstomagoPageDTO(estomagos, pageEstomago.getTotalPages(), pageEstomago.getTotalElements());
    }

    public EstomagoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

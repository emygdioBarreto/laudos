package br.com.laudos.service;

import br.com.laudos.domain.Esofago;
import br.com.laudos.dto.EsofagoDTO;
import br.com.laudos.dto.mapper.EsofagoMapper;
import br.com.laudos.dto.pages.EsofagoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.EsofagoRepository;
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
public class EsofagoService {

    private final EsofagoRepository repository;
    private final EsofagoMapper mapper;

    public EsofagoService(EsofagoRepository repository, EsofagoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EsofagoDTO salvar(@Valid @NotNull EsofagoDTO esofagoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(esofagoDTO)));
    }

    public EsofagoDTO update(@NotNull @Positive Integer id, @Valid @NotNull EsofagoDTO esofagoDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setDescricao(esofagoDTO.descricao());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public EsofagoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Esofago> pageEsofago = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<EsofagoDTO> esofagos = pageEsofago.get().map(mapper::toDTO).toList();
        return new EsofagoPageDTO(esofagos,pageEsofago.getTotalPages(), pageEsofago.getTotalElements());
    }

    public EsofagoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

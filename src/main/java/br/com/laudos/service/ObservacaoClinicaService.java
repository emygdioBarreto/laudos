package br.com.laudos.service;

import br.com.laudos.domain.ObservacaoClinica;
import br.com.laudos.dto.ObservacaoClinicaDTO;
import br.com.laudos.dto.mapper.ObservacaoClinicaMapper;
import br.com.laudos.dto.pages.ObservacaoClinicaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ObservacaoClinicaRepository;
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
public class ObservacaoClinicaService {

    private final ObservacaoClinicaRepository repository;
    private final ObservacaoClinicaMapper mapper;

    public ObservacaoClinicaDTO salvar(@Valid @NotNull ObservacaoClinicaDTO observacaoClinicaDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(observacaoClinicaDTO)));
    }

    public ObservacaoClinicaDTO update(@NotNull @Positive Integer id,
                                       @Valid @NotNull ObservacaoClinicaDTO observacaoClinicaDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setDescricao(observacaoClinicaDTO.descricao());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public ObservacaoClinicaPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<ObservacaoClinica> pageObservacaoClinica = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<ObservacaoClinicaDTO> observacoesClinicas = pageObservacaoClinica.get().map(mapper::toDTO).toList();
        return new ObservacaoClinicaPageDTO(observacoesClinicas, pageObservacaoClinica.getTotalPages(), pageObservacaoClinica.getTotalElements());
    }

    public ObservacaoClinicaDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));

    }
}

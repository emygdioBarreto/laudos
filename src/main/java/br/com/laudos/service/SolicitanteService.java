package br.com.laudos.service;

import br.com.laudos.domain.Solicitante;
import br.com.laudos.dto.SolicitanteDTO;
import br.com.laudos.dto.mapper.SolicitanteMapper;
import br.com.laudos.dto.pages.SolicitantePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.SolicitanteRepository;
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
public class SolicitanteService {

    private final SolicitanteRepository repository;
    private final SolicitanteMapper mapper;

    public SolicitanteDTO salvar(@Valid @NotNull SolicitanteDTO solicitanteDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(solicitanteDTO)));
    }

    public SolicitanteDTO update(@NotNull @Positive Integer id, @Valid @NotNull SolicitanteDTO solicitanteDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setMedicoSolicitante(solicitanteDTO.medicoSolicitante());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public SolicitantePageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Solicitante> pageSolicitante = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<SolicitanteDTO> solicitantes = pageSolicitante.get().map(mapper::toDTO).toList();
        return new SolicitantePageDTO(solicitantes, pageSolicitante.getTotalPages(), pageSolicitante.getTotalElements());
    }

    public SolicitanteDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

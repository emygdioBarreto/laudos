package br.com.laudos.service;

import br.com.laudos.domain.Resumo;
import br.com.laudos.dto.ResumoDTO;
import br.com.laudos.dto.mapper.ResumoMapper;
import br.com.laudos.dto.pages.ResumoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ResumoRepository;
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
public class ResumoService {

    private final ResumoRepository repository;
    private final ResumoMapper mapper;

    public ResumoDTO salvar(@Valid @NotNull ResumoDTO resumoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(resumoDTO)));
    }

    public ResumoDTO update(@NotNull @Positive Integer id,
                            @Valid @NotNull ResumoDTO resumoDTO) {
        return repository.findById(id)
                .map(reg -> {
                    reg.setDescricao(resumoDTO.descricao());
                    return mapper.toDTO(repository.save(reg));
                }).orElseThrow(()-> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(id)));
    }

    public ResumoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Resumo> pageResumo = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<ResumoDTO> resumos = pageResumo.get().map(mapper::toDTO).toList();
        return new ResumoPageDTO(resumos, pageResumo.getTotalPages(), pageResumo.getTotalElements());
    }

    public ResumoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(()-> new RecordNotFoundException(id));
    }
}

package br.com.laudos.service;

import br.com.laudos.domain.Concluir;
import br.com.laudos.dto.ConcluirDTO;
import br.com.laudos.dto.mapper.ConcluirMapper;
import br.com.laudos.dto.pages.ConcluirPageDTO;
import br.com.laudos.exceptions.RecordFoundException;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ConcluirRepository;
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
public class ConcluirService {

    private final ConcluirRepository repository;
    private final ConcluirMapper mapper;

    public ConcluirDTO salvar(@NotNull @Valid ConcluirDTO concluirDTO) {
        boolean fraseJaExiste = repository.existsByConclusao(concluirDTO.conclusao());
        if (fraseJaExiste) {
            throw new RecordFoundException("Frase de Conclusão já cadastrada!  (" + concluirDTO.conclusao() + ")");
        }
        return mapper.toDTO(repository.save(mapper.toEntity(concluirDTO)));
    }

    public ConcluirDTO update(@NotNull @Positive Integer id, @Valid @NotNull ConcluirDTO concluirDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setConclusao(concluirDTO.conclusao());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public ConcluirPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Concluir> pageConcluir = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<ConcluirDTO> conclusoes = pageConcluir.get().map(mapper::toDTO).toList();
        return new ConcluirPageDTO(conclusoes, pageConcluir.getTotalPages(), pageConcluir.getTotalElements());
    }

    public ConcluirDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

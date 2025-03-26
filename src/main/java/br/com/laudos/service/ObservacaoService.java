package br.com.laudos.service;

import br.com.laudos.domain.Observacao;
import br.com.laudos.dto.ObservacaoDTO;
import br.com.laudos.dto.mapper.ObservacaoMapper;
import br.com.laudos.dto.pages.ObservacaoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ObservacaoRepository;
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
public class ObservacaoService {

    private final ObservacaoRepository repository;
    private final ObservacaoMapper mapper;

    public ObservacaoDTO salvar(@Valid @NotNull ObservacaoDTO observacaoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(observacaoDTO)));
    }

    public ObservacaoDTO update(@NotNull @Positive Integer id, @Valid @NotNull ObservacaoDTO observacaoDTO) {
        return  repository.findById(id)
                .map(reg -> {
                    reg.setDescricao(observacaoDTO.descricao());
                    return mapper.toDTO(repository.save(reg));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public ObservacaoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Observacao> pageObservacao = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<ObservacaoDTO> observacoes = pageObservacao.get().map(mapper::toDTO).toList();
        return new ObservacaoPageDTO(observacoes, pageObservacao.getTotalPages(), pageObservacao.getTotalElements());
    }

    public ObservacaoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

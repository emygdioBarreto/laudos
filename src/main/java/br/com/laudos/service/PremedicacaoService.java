package br.com.laudos.service;

import br.com.laudos.domain.Premedicacao;
import br.com.laudos.dto.PremedicacaoDTO;
import br.com.laudos.dto.mapper.PremedicacaoMapper;
import br.com.laudos.dto.pages.PremedicacaoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.PremedicacaoRepository;
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
public class PremedicacaoService {

    private final PremedicacaoRepository repository;
    private final PremedicacaoMapper mapper;

    public PremedicacaoService(PremedicacaoRepository repository, PremedicacaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PremedicacaoDTO salvar(@Valid @NotNull PremedicacaoDTO premedicacaoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(premedicacaoDTO)));
    }

    public PremedicacaoDTO update(@NotNull @Positive Integer id,
                                  @Valid @NotNull PremedicacaoDTO premedicacaoDTO) {
        return repository.findById(id)
                .map(reg -> {
                    reg.setAnalgesia(premedicacaoDTO.analgesia());
                    return mapper.toDTO(repository.save(reg));
                }).orElseThrow(()-> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(id)));
    }

    public PremedicacaoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Premedicacao> pagePremedicacao = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<PremedicacaoDTO> premedicacoes = pagePremedicacao.get().map(mapper::toDTO).toList();
        return new PremedicacaoPageDTO(premedicacoes, pagePremedicacao.getTotalPages(), pagePremedicacao.getTotalElements());
    }

    public PremedicacaoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(()-> new RecordNotFoundException(id));
    }
}

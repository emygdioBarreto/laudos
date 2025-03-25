package br.com.laudos.service;

import br.com.laudos.domain.Equipamento;
import br.com.laudos.dto.EquipamentoDTO;
import br.com.laudos.dto.mapper.EquipamentoMapper;
import br.com.laudos.dto.pages.EquipamentoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.EquipamentoRepository;
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
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final EquipamentoMapper mapper;

    public EquipamentoService(EquipamentoRepository repository, EquipamentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EquipamentoDTO salvar(@Valid @NotNull EquipamentoDTO equipamentoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(equipamentoDTO)));
    }

    public EquipamentoDTO update(@NotNull @Positive Integer id, @Valid @NotNull EquipamentoDTO equipamentoDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setDescricao(equipamentoDTO.descricao());
                    recordFound.setSuperior(equipamentoDTO.superior());
                    recordFound.setInferior(equipamentoDTO.inferior());
                    recordFound.setDireita(equipamentoDTO.direita());
                    recordFound.setEsquerda(equipamentoDTO.esquerda());
                    recordFound.setModLaudo(equipamentoDTO.modLaudo());
                    recordFound.setCidade(equipamentoDTO.cidade());
                    recordFound.setOrdena(equipamentoDTO.ordena());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public EquipamentoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Equipamento> pageEquipamento = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<EquipamentoDTO> equipamentos = pageEquipamento.get().map(mapper::toDTO).toList();
        return new EquipamentoPageDTO(equipamentos, pageEquipamento.getTotalPages(), pageEquipamento.getTotalElements());
    }

    public EquipamentoDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

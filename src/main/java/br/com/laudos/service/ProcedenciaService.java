package br.com.laudos.service;

import br.com.laudos.domain.Procedencia;
import br.com.laudos.dto.ProcedenciaDTO;
import br.com.laudos.dto.mapper.ProcedenciaMapper;
import br.com.laudos.dto.pages.ProcedenciaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ProcedenciaRepository;
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
public class ProcedenciaService {

    private final ProcedenciaRepository repository;
    private final ProcedenciaMapper mapper;

    public ProcedenciaDTO salvar(@Valid @NotNull ProcedenciaDTO procedenciaDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(procedenciaDTO)));
    }

    public ProcedenciaDTO update(@NotNull @Positive Integer id, @Valid @NotNull ProcedenciaDTO procedenciaDTO) {
        return repository.findById(id)
                .map(reg -> {
                    reg.setDescricao(procedenciaDTO.descricao());
                    return mapper.toDTO(repository.save(reg));
                }).orElseThrow(()-> new RecordNotFoundException(id));
    }

    public void delete(Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(id)));
    }

    public ProcedenciaPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Procedencia> pageProcedencia = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<ProcedenciaDTO> procedencias = pageProcedencia.get().map(mapper::toDTO).toList();
        return new ProcedenciaPageDTO(procedencias, pageProcedencia.getTotalPages(), pageProcedencia.getTotalElements());
    }

    public List<ProcedenciaDTO> findAllProcedencias() throws RecordNotFoundException {
        try {
            return repository.findAllProcedencias().stream().map(mapper::toDTO).toList();
        } catch (java.lang.RuntimeException e) {
            throw new RecordNotFoundException("Registros não encontrados. Verifique conexão com banco de dados!");
        }
    }

    public ProcedenciaDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(()-> new RecordNotFoundException(id));
    }
}

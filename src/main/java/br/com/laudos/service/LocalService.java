package br.com.laudos.service;

import br.com.laudos.domain.Local;
import br.com.laudos.dto.LocalDTO;
import br.com.laudos.dto.mapper.LocalMapper;
import br.com.laudos.dto.pages.LocalPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.LocalRepository;
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
public class LocalService {

    private final LocalRepository repository;
    private final LocalMapper mapper;

    public LocalDTO salvar(@Valid @NotNull LocalDTO localDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(localDTO)));
    }

    public LocalDTO update(@NotNull @Positive Integer id, @Valid @NotNull LocalDTO localDTO) {
        return repository.findById(id)
                .map(registro -> {
                    registro.setDescricao(localDTO.descricao());
                    return mapper.toDTO(repository.save(registro));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public LocalPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Local> pageLocal= repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<LocalDTO> locais = pageLocal.get().map(mapper::toDTO).toList();
        return new LocalPageDTO(locais, pageLocal.getTotalPages(), pageLocal.getTotalElements());
    }

    public List<LocalDTO> findAllLocais() throws RecordNotFoundException {
        try {
            return repository.findAllLocais().stream().map(mapper::toDTO).toList();
        } catch (java.lang.RuntimeException e) {
            throw new RecordNotFoundException("Registros não encontrados. Verifique conexão com banco de dados!");
        }
    }

    public LocalDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

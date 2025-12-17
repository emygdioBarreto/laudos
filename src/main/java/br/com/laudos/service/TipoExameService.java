package br.com.laudos.service;

import br.com.laudos.domain.TipoExame;import br.com.laudos.dto.TipoExameDTO;
import br.com.laudos.dto.mapper.TipoExameMapper;
import br.com.laudos.dto.pages.TipoExamePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.TipoExameRepository;
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
public class TipoExameService {

    private final TipoExameRepository repository;
    private final TipoExameMapper mapper;

    public TipoExameDTO salvar(@Valid @NotNull TipoExameDTO tipoExameDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(tipoExameDTO)));
    }

    public TipoExameDTO update(@NotNull @Positive Integer id, @Valid @NotNull TipoExameDTO tipoExameDTO) {
        return repository.findById(id)
                .map(recordFound -> {
                    recordFound.setDescricao(tipoExameDTO.descricao());
                    recordFound.setOrdena(tipoExameDTO.ordena());
                    recordFound.setEsofago(tipoExameDTO.esofago());
                    recordFound.setEstomago(tipoExameDTO.estomago());
                    recordFound.setDuodeno(tipoExameDTO.duodeno());
                    recordFound.setIntestino(tipoExameDTO.intestino());
                    recordFound.setPancreas(tipoExameDTO.pancreas());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Integer id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public TipoExamePageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<TipoExame> pageTipoExame = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<TipoExameDTO> tiposExames = pageTipoExame.get().map(mapper::toDTO).toList();
        return new TipoExamePageDTO(tiposExames, pageTipoExame.getTotalPages(), pageTipoExame.getTotalElements());
    }

    public List<TipoExameDTO> findAllTipoExames() throws RecordNotFoundException {
        try {
            return repository.findAllTipoExames().stream().map(mapper::toDTO).toList();
        } catch (java.lang.RuntimeException e) {
            throw new RecordNotFoundException("Registros não encontrados. Verifique conexão com banco de dados!");
        }
    }

    public TipoExameDTO findById(@NotNull @Positive Integer id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

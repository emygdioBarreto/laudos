package br.com.laudos.service;

import br.com.laudos.domain.Medico;
import br.com.laudos.dto.MedicoDTO;
import br.com.laudos.dto.mapper.MedicoMapper;
import br.com.laudos.dto.pages.MedicoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.MedicoRepository;
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
public class MedicoService {

    private final MedicoRepository repository;
    private final MedicoMapper mapper;

    public MedicoDTO salvar(@Valid @NotNull MedicoDTO medicoDTO) {
        return mapper.toDTO(repository.save(mapper.toEntity(medicoDTO)));
    }

    public MedicoDTO update(@NotNull String crm, @Valid @NotNull MedicoDTO medicoDTO) {
        return repository.findById(crm)
                .map(recordFound -> {
                    recordFound.setMedicoExecutor(medicoDTO.medicoExecutor());
                    return mapper.toDTO(repository.save(recordFound));
                }).orElseThrow(() -> new RecordNotFoundException(crm));
    }

    public void delete(@NotNull String crm) {
        repository.delete(repository.findById(crm)
                .orElseThrow(() -> new RecordNotFoundException(crm)));
    }

    public MedicoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Medico> pageMedico = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "crm")));
        List<MedicoDTO> medicos = pageMedico.get().map(mapper::toDTO).toList();
        return new MedicoPageDTO(medicos, pageMedico.getTotalPages(), pageMedico.getTotalElements());
    }

    public List<MedicoDTO> findAllMedicos() throws RecordNotFoundException {
        try {
            return repository.findAllMedicos().stream().map(mapper::toDTO).toList();
        } catch (java.lang.RuntimeException e) {
            throw new RecordNotFoundException("Registros não encontrados. Verifique conexão com banco de dados!");
        }
    }

    public MedicoDTO findById(@NotNull String crm) {
        return repository.findById(crm).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(crm));
    }
}

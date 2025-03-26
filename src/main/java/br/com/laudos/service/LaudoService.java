package br.com.laudos.service;

import br.com.laudos.domain.Laudo;
import br.com.laudos.dto.LaudoDTO;
import br.com.laudos.dto.mapper.LaudoMapper;
import br.com.laudos.dto.pages.LaudoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.LaudoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private final LaudoRepository repository;
    private final LaudoMapper mapper;

    public LaudoDTO salvar(@Valid @NotNull LaudoDTO laudoDTO) throws ParseException {
        return mapper.toDTO(repository.save(mapper.toEntity(laudoDTO)));
    }

    public LaudoDTO update(@NotNull @Positive Long id, @Valid @NotNull LaudoDTO laudoDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        return repository.findById(id)
                .map(laudo -> {
                    try {
                        laudo.setData(sdf.parse(laudoDTO.data()));
                        laudo.setEquipamento(laudoDTO.equipamento());
                        laudo.setPaciente(laudoDTO.paciente());
                        laudo.setIdade(laudo.getIdade());
                        laudo.setNascimento(sdf.parse(laudoDTO.nascimento()));
                        laudo.setSexo(laudoDTO.sexo());
                        laudo.setSolicitante(laudoDTO.solicitante());
                        laudo.setProcedencia(laudoDTO.procedencia());
                        laudo.setPremedicacao(laudoDTO.premedicacao());
                        laudo.setLocalExame(laudoDTO.localExame());
                        laudo.setMedicoExecutor(laudoDTO.medicoExecutor());
                        laudo.setResumo(laudoDTO.resumo());
                        laudo.setObservacaoClinica(laudoDTO.observacaoClinica());
                        laudo.setEsofago(laudoDTO.esofago());
                        laudo.setEstomago(laudoDTO.estomago());
                        laudo.setDuodeno(laudoDTO.duodeno());
                        laudo.setIntestino(laudoDTO.intestino());
                        laudo.setPancreas(laudoDTO.pancreas());
                        laudo.setSolucao(laudoDTO.solucao());
                        laudo.setConclusao(laudoDTO.conclusao());
                        laudo.setObservacao(laudoDTO.observacao());
                        laudo.setTipoExame(laudoDTO.tipoExame());
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Falha na conversão de datas");
                    }
                    return mapper.toDTO(repository.save(laudo));
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id)));
    }

    public LaudoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Laudo> pageLaudo = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        List<LaudoDTO> laudos = pageLaudo.get().map(mapper::toDTO).toList();
        return new LaudoPageDTO(laudos, pageLaudo.getTotalPages(), pageLaudo.getTotalElements());
    }

    public LaudoDTO findById(@NotNull @Positive Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}

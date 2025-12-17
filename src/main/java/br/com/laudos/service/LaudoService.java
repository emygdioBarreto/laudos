package br.com.laudos.service;

import br.com.laudos.domain.Laudo;
import br.com.laudos.dto.LaudoDTO;
import br.com.laudos.dto.LaudoUpdateDTO;
import br.com.laudos.dto.mapper.LaudoMapper;
import br.com.laudos.dto.pages.LaudoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private static final String LAUDO_NAO_ENCONTRADO = "Laudo não encontrado";

    private final EquipamentoRepository equipamentoRepository;
    private final LaudoRepository repository;
    private final LocalRepository localRepository;
    private final MedicoRepository medicoRepository;
    private final ProcedenciaRepository procedenciaRepository;
    private final PremedicacaoRepository premedicacaoRepository;
    private final SolicitanteRepository solicitanteRepository;
    private final ResumoRepository resumoRepository;
    private final TipoExameRepository tipoExameRepository;
    private final LaudoMapper mapper;

    public LaudoDTO salvar(@Valid @NotNull LaudoDTO dto) {
        Laudo laudo = repository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException(LAUDO_NAO_ENCONTRADO));
        laudo.setEquipamento(equipamentoRepository.getReferenceById(dto.equipamento().getId()));
        laudo.setLocalExame(localRepository.getReferenceById(dto.localExame().getId()));
        laudo.setTipoExame(tipoExameRepository.getReferenceById(dto.tipoExame().getId()));
        laudo.setProcedencia(procedenciaRepository.getReferenceById(dto.procedencia().getId()));
        laudo.setResumo(resumoRepository.getReferenceById(dto.resumo().getId()));
        laudo.setSolicitante(solicitanteRepository.getReferenceById(dto.solicitante().getId()));
        laudo.setMedicoExecutor(medicoRepository.getReferenceById(dto.medicoExecutor().getCrm()));

        // textos
        laudo.setEsofago(dto.esofago());
        laudo.setEstomago(dto.estomago());
        laudo.setDuodeno(dto.duodeno());
        laudo.setIntestino(dto.intestino());
        laudo.setPancreas(dto.pancreas());
        return mapper.toDTO(repository.save(laudo));
    }

    @Transactional
    public LaudoDTO update(@NotNull @Positive Long id, @Valid @NotNull LaudoUpdateDTO dto) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Laudo laudo = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(LAUDO_NAO_ENCONTRADO));

        laudo.setData(sdf.parse(dto.data()));
        laudo.setEquipamento(equipamentoRepository.getReferenceById(dto.equipamentoId()));
        laudo.setSolicitante(solicitanteRepository.getReferenceById(dto.solicitanteId()));
        laudo.setProcedencia(procedenciaRepository.getReferenceById(dto.procedenciaId()));
        laudo.setPremedicacao(premedicacaoRepository.getReferenceById(dto.premedicacaoId()));
        laudo.setLocalExame(localRepository.getReferenceById(dto.localExameId()));
        laudo.setResumo(resumoRepository.getReferenceById(dto.resumoId()));
        laudo.setTipoExame(tipoExameRepository.getReferenceById(dto.tipoExameId()));
        laudo.setMedicoExecutor(medicoRepository.findByCrm(dto.medicoExecutorCrm()));
        // campos simples
        laudo.setPaciente(dto.paciente());
        laudo.setIdade(dto.idade());
        laudo.setNascimento(sdf.parse(dto.nascimento()));
        laudo.setSexo(dto.sexo());
        laudo.setObservacaoClinica(dto.observacaoClinica());
        laudo.setEsofago(dto.esofago());
        laudo.setEstomago(dto.estomago());
        laudo.setDuodeno(dto.duodeno());
        laudo.setIntestino(dto.intestino());
        laudo.setPancreas(dto.pancreas());
        laudo.setConclusao(dto.conclusao());
        laudo.setObservacao(dto.observacao());
        return mapper.toDTO(repository.save(laudo));
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

    @Transactional(readOnly = true)
    public LaudoDTO findById(@NotNull @Positive Long id) {
        return repository.buscarCompletoPorId(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(LAUDO_NAO_ENCONTRADO));
    }
}

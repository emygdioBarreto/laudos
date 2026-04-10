package br.com.laudos.service;

import br.com.laudos.domain.*;
import br.com.laudos.dto.*;
import br.com.laudos.dto.mapper.LaudoMapper;
import br.com.laudos.dto.mapper.LaudoRefs;
import br.com.laudos.dto.pages.LaudoPageDTO;
import br.com.laudos.exceptions.GlobalExceptionHandler;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
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
    private final SpringTemplateEngine templateEngine;

    // Injeta a URL configurada nos arquivos YML
    @Value("${app.urlValidacao}")
    private String urlBaseCertificado;

    @Transactional
    public LaudoDTO salvar(@Valid @NotNull LaudoCreateDTO createDTO) {
        LaudoRefs relacoes = resolveRelacionamentosValidados(createDTO);
        Laudo laudo = mapper.toEntityCreate(createDTO, relacoes);
        return mapper.toDTO(repository.save(laudo));
    }

    @Transactional
    public LaudoDTO update(@NotNull @Positive Long idLaudo, @Valid @NotNull LaudoUpdateDTO updateDTO) {
        Laudo laudo = repository.findById(idLaudo)
                .orElseThrow(() -> new EntityNotFoundException(LAUDO_NAO_ENCONTRADO));

        LaudoRefs relacoes = resolveRelacionamentosValidados(updateDTO);
        mapper.toEntityUpdate(laudo, updateDTO, relacoes);
        return mapper.toDTO(repository.save(laudo));
    }

    public void delete(@NotNull @Positive Long idLaudo) {
        repository.delete(repository.findById(idLaudo)
                .orElseThrow(() -> new RecordNotFoundException(idLaudo)));
    }

    public LaudoPageDTO findAll(@PositiveOrZero int page, @Positive int pageSize) {
        Page<Laudo> pageLaudo = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "idLaudo")));
        List<LaudoDTO> laudos = pageLaudo.get().map(mapper::toDTO).toList();
        return new LaudoPageDTO(laudos, pageLaudo.getTotalPages(), pageLaudo.getTotalElements());
    }

    public LaudoDTO findById(@NotNull @Positive Long idLaudo) {
        return repository.buscarCompletoPorId(idLaudo).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(LAUDO_NAO_ENCONTRADO));
    }

    // =========================
    // RESOLVERS
    // =========================
    private LaudoRefs resolveRelacionamentosValidados(LaudoCreateDTO dto) {
        return resolveRelacionamentosValidados(new LaudoIdsDTO(
                dto.equipamentoId(),
                dto.solicitanteId(),
                dto.procedenciaId(),
                dto.premedicacaoId(),
                dto.localExameId(),
                dto.tipoExameId(),
                dto.medicoExecutorCrm(),
                dto.resumoId()
        ));
    }

    private LaudoRefs resolveRelacionamentosValidados(LaudoUpdateDTO dto) {
        return resolveRelacionamentosValidados(new LaudoIdsDTO(
                dto.equipamentoId(),
                dto.solicitanteId(),
                dto.procedenciaId(),
                dto.premedicacaoId(),
                dto.localExameId(),
                dto.tipoExameId(),
                dto.medicoExecutorCrm(),
                dto.resumoId()
        ));
    }

    private LaudoRefs resolveRelacionamentosValidados(LaudoIdsDTO idsDTO) {
        return new LaudoRefs(
                loadEquipamento(idsDTO.equipamentoId()),
                loadSolicitante(idsDTO.solicitanteId()),
                loadProcedencia(idsDTO.procedenciaId()),
                loadPremedicacao(idsDTO.premedicacaoId()),
                loadLocal(idsDTO.localExameId()),
                loadTipoExame(idsDTO.tipoExameId()),
                loadMedico(idsDTO.medicoExecutorCrm()),
                loadResumo(idsDTO.resumoId())
        );
    }

    // =========================
    // LOADERS
    // =========================
    private Equipamento loadEquipamento(Long id) {
        return equipamentoRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Equipamento não encontrado: " + id));
    }

    private Local loadLocal(Long id) {
        return localRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Local do Exame não encontrado: " + id));
    }

    private Medico loadMedico(String crm) {
        return medicoRepository.findById(crm)
                .orElseThrow(() ->
                        new EntityNotFoundException("Médico não encontrado: CRM " + crm));
    }

    private Premedicacao loadPremedicacao(Long id) {
        return premedicacaoRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Premedicação não encontrada: id " + id));
    }

    private Procedencia loadProcedencia(Long id) {
        return procedenciaRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Procedência não encontrada: id " + id));
    }

    private Resumo loadResumo(Long id) {
        return resumoRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Resumo Clínico não encontrado: id " + id));
    }

    private Solicitante loadSolicitante(Long id) {
        return solicitanteRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Solicitante do Exame não encontrado: id " + id));
    }

    private TipoExame loadTipoExame(Long id) {
        return tipoExameRepository.findById(id.intValue())
                .orElseThrow(() ->
                        new EntityNotFoundException("Tipo de Exame não encontrado: id " + id));
    }

    @Transactional
    public byte[] gerarPdf(Long idLaudo) throws IOException {
        // 1. Busca a entidade no banco de dados
        Laudo laudo = repository.findById(idLaudo)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado: " + idLaudo));

        // 2. Saneamento e Persistência Imediata
        if (laudo.getHashValidacao() == null || laudo.getHashValidacao().isBlank()) {
            String novoHash = UUID.randomUUID().toString();
            laudo.setHashValidacao(novoHash);
            laudo = repository.saveAndFlush(laudo);
        }

        String hashOficial = laudo.getHashValidacao();

        // Converte para DTO
        LprintDTO dadosParaImpressao = mapearParaLprint(laudo);

        // Usa a URL dinâmica injetada do YML
        String urlValidacao = urlBaseCertificado + hashOficial;

        // QR CODE (Gera o QRCode com a nova URL)
        String qrCodeBase64 = gerarQrCodeBase64(urlValidacao);

        // Prepara o contexto do Thymeleaf
        Context context = new Context();
        context.setVariable("laudo", dadosParaImpressao);
        context.setVariable("qrCode", qrCodeBase64);
        context.setVariable("urlValidacao", urlValidacao);

        // Processa o HTML e gera o PDF (Restante do seu código...)
        String htmlFormatado = templateEngine.process("laudos/modelo_exame", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlFormatado, "/");
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }
    }

    /**
     * Metodo auxiliar para transformar a Entidade complexa no Record flat Lprint.
     * Isso isola a lógica de formatação de strings e cálculos da regra de negócio.
     */
    private LprintDTO mapearParaLprint(Laudo laudo) {
        return new LprintDTO(
                laudo.getIdLaudo().intValue(),
                laudo.getDataCriacao().atStartOfDay().toLocalDate(),
                laudo.getEquipamento() != null ? laudo.getEquipamento().getDescricao() : "",
                laudo.getPaciente(),
                laudo.getIdade(),
                laudo.getNascimento(),
                laudo.getSexo(),
                laudo.getSolicitante().getMedicoSolicitante() != null ? laudo.getSolicitante().getMedicoSolicitante() : "",
                laudo.getProcedencia() != null ? laudo.getProcedencia().getDescricao() : "",
                laudo.getPremedicacao() != null ? laudo.getPremedicacao().getDescricao() : "",
                laudo.getLocalExame() != null ? laudo.getLocalExame().getDescricao() : "",
                laudo.getMedicoExecutor() != null ? laudo.getMedicoExecutor().getMedicoExecutor() : "",
                laudo.getResumo() != null ? laudo.getResumo().getDescricao() : "",
                corrigirTexto(laudo.getEsofago()),
                corrigirTexto(laudo.getEstomago()),
                corrigirTexto(laudo.getDuodeno()),
                corrigirTexto(laudo.getIntestino()),
                corrigirTexto(laudo.getPancreas()),
                corrigirTexto(laudo.getSolucao()),
                corrigirTexto(laudo.getConclusao()),
                corrigirTexto(laudo.getObservacao()),
                laudo.getObservacaoClinica(),
                laudo.getMedicoExecutor() != null ? laudo.getMedicoExecutor().getCrm() : "",
                laudo.getTipoExame() != null ? laudo.getTipoExame().getDescricao() : ""
        );
    }

    private String corrigirTexto(String texto) {
        if (texto == null) return null;

        return texto
                .replaceAll("\\.(\\S)", ". $1")   // adiciona espaço após ponto
                .replaceAll("\\s+", " ")          // remove espaços duplicados
                .trim();
    }

    private String gerarQrCodeBase64(String texto) {
        try {
            int width = 150;
            int height = 150;

            com.google.zxing.common.BitMatrix matrix =
                    new com.google.zxing.qrcode.QRCodeWriter()
                            .encode(texto, com.google.zxing.BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);

            return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());

        } catch (Exception e) {
            throw new GlobalExceptionHandler.LaudoServiceException("Erro ao gerar QR Code", e);
        }
    }

    /**
     * Busca um laudo pelo hash e converte para o DTO simplificado de validação
     */
    public LaudoValidacaoDTO buscarDadosParaValidacao(String hash) {
        return repository.findByHashValidacao(hash)
                .map(mapper::toDTOValidacao) // Chama o metodo de conversão abaixo
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com o hash: " + hash));
    }
}

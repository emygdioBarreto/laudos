package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.domain.Laudo;
import br.com.laudos.dto.LaudoCreateDTO;
import br.com.laudos.dto.LaudoDTO;
import br.com.laudos.dto.LaudoUpdateDTO;
import br.com.laudos.dto.LaudoValidacaoDTO;
import br.com.laudos.dto.pages.LaudoPageDTO;
import br.com.laudos.repository.LaudoRepository;
import br.com.laudos.service.LaudoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Validated
@RestController
@RequestMapping("/laudos")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Laudos", description = "Método para salvar, editar, listar e remover Laudos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class LaudoController {

    private final LaudoService service;
    private final LaudoRepository repository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Laudo", description = "Método para salvar o Laudo preenchido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Laudo salvo com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Laudo já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<LaudoDTO> salvar(@RequestBody @Valid @NotNull LaudoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{idLaudo}")
    @Operation(summary = "Atualizar Laudo", description = "Método para atualizar o Laudo selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Laudo atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização do Laudo selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<LaudoDTO> update(
            @PathVariable @NotNull @Positive Long idLaudo,
            @RequestBody @Valid @NotNull LaudoUpdateDTO laudoUpdateDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(idLaudo, laudoUpdateDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{idLaudo}")
    @Operation(summary = "Remover Laudo", description = "Método para remover Laudo selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laudo removido com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do Laudo selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Long idLaudo) {
        service.delete(idLaudo);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar Laudos", description = "Método para Listar todos os Laudos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Laudos com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga dos Laudos"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LaudoPageDTO list(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                             @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{idLaudo}")
    @Operation(summary = "Buscar Laudo por ID", description = "Método para buscar Laudo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laudo localizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do Laudo selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LaudoDTO findById(@PathVariable @NotNull @Positive Long idLaudo) {
        return service.findById(idLaudo);
    }

    @Transactional(readOnly = true)
    @GetMapping("/validar/{hash}")
    public ModelAndView validarLaudo(@PathVariable String hash) {

        Laudo laudo = repository.findByHashValidacao(hash)
                .orElseThrow(() -> new RuntimeException("Laudo não encontrado"));

        ModelAndView mv = new ModelAndView("validacao");

        mv.addObject("paciente", laudo.getPaciente());
        mv.addObject("data", laudo.getDataCriacao());
        mv.addObject("medico", laudo.getMedicoExecutor().getMedicoExecutor());
        mv.addObject("crm", laudo.getMedicoExecutor().getCrm());
        mv.addObject("status", "VÁLIDO");

        return mv;
    }

    @Transactional(readOnly = true)
    @GetMapping("/validar-dados/{hash}")
    @Operation(summary = "Validar Laudo via API", description = "Retorna dados simplificados para validação em modal")
    public ResponseEntity<LaudoValidacaoDTO> validar(@PathVariable String hash) {
        try {
            LaudoValidacaoDTO dto = service.buscarDadosParaValidacao(hash);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o hash não existir
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{idLaudo}/pdf")
    @Operation(summary = "Gerar PDF do Laudo", description = "Método para gerar o arquivo PDF do laudo selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Laudo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar o PDF")
    })
    public ResponseEntity<byte[]> gerarPdf(@PathVariable @NotNull @Positive Long idLaudo) {
        try {

            // 🔥 BUSCA UMA VEZ SÓ
            LaudoDTO laudo = service.findById(idLaudo);

            // 🔥 GERA PDF (se puder, passe o DTO pra evitar nova consulta interna)
            byte[] pdfContents = service.gerarPdf(idLaudo);

            // 🔥 DATA DO LAUDO
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String dataFormatada = (laudo.getDataCriacao() != null)
                    ? laudo.getDataCriacao().format(formatter)
                    : LocalDate.now().format(formatter);

            // 🔥 NOME FINAL BONITO
            String nomeArquivo = "Laudo_"
                    + normalizar(laudo.getPaciente())
                    + "_"
                    + idLaudo
                    + "_"
                    + dataFormatada
                    + ".pdf";

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + nomeArquivo + "\"")
                    .body(pdfContents);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String normalizar(String texto) {
        if (texto == null) return "LAUDO";

        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")   // remove acentos
                .replaceAll("[^a-zA-Z0-9]", "_")   // troca caracteres inválidos
                .replaceAll("_+", "_")             // remove duplicação
                .toUpperCase();
    }
}

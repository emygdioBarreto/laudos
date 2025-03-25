package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.PremedicacaoDTO;
import br.com.laudos.dto.pages.PremedicacaoPageDTO;
import br.com.laudos.service.PremedicacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/premedicacoes")
@CrossOrigin("*")
@Tag(name = "Frases de Premedicações", description = "Método para salvar, editar, listar e remover dados de frases de Premedicações")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class PremedicacaoController {

    private final PremedicacaoService service;

    public PremedicacaoController(PremedicacaoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Premedicação", description = "Método para salvar a frase de Premedicação preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Premedicação salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Premedicação já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<PremedicacaoDTO> salvar(@RequestBody @Valid @NotNull PremedicacaoDTO premedicacaoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(premedicacaoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Premedicação", description = "Método para atualizar a frase de Premedicação selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Premedicação atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Premedicação selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public PremedicacaoDTO update(@PathVariable @NotNull @Positive Integer id,
                                  @RequestBody @Valid @NotNull PremedicacaoDTO premedicacaoDTO) {
        return service.update(id, premedicacaoDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Premedicação", description = "Método para remover frases de Premedicação selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Premedicação removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Premedicação selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Premedicações", description = "Método para Listar todas as frases de Premedicações cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Premedicações carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Premedicações"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public PremedicacaoPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                       @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page,pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Premedicação por ID", description = "Método para buscar a frase de Premedicação por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Premedicação localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Premedicação selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public PremedicacaoDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

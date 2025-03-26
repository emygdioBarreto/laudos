package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.ProcedenciaDTO;
import br.com.laudos.dto.pages.ProcedenciaPageDTO;
import br.com.laudos.service.ProcedenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/procedencias")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Procedências", description = "Método para salvar, editar, listar e remover dados de frases de Procedências")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ProcedenciaController {

    private final ProcedenciaService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Procedência", description = "Método para salvar a frase de Procedência preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Procedência salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Procedência já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<ProcedenciaDTO> salvar(@RequestBody ProcedenciaDTO procedenciaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(procedenciaDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Procedência", description = "Método para atualizar a frase de Procedência selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Procedência atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Procedência selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ProcedenciaDTO update(@PathVariable @NotNull @Positive Integer id,
                                 @RequestBody @Valid @NotNull ProcedenciaDTO procedenciaDTO) {
         return service.update(id, procedenciaDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Procedência", description = "Método para remover frases de Procedência selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Procedência removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Procedência selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Procedências", description = "Método para Listar todas as frases de Procedências cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Procedências carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Procedências"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ProcedenciaPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                      @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Procedência por ID", description = "Método para buscar a frase de Procedência por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Procedência localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Procedência selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ProcedenciaDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

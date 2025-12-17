package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.SolicitanteDTO;
import br.com.laudos.dto.pages.SolicitantePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.SolicitanteService;
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

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/solicitantes")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Solicitantes", description = "Método para salvar, editar, listar e remover dados de frases de Solicitantes")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class SolicitanteController {

    private final SolicitanteService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Solicitante", description = "Método para salvar a frase de Solicitante preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Solicitante salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Solicitante já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<SolicitanteDTO> salvar(@RequestBody @Valid @NotNull SolicitanteDTO solicitanteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(solicitanteDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Solicitante", description = "Método para atualizar a frase de Solicitante selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Solicitante atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Solicitante selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<SolicitanteDTO> update(
            @PathVariable @NotNull @Positive Integer id,
            @RequestBody @Valid @NotNull SolicitanteDTO solicitanteDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(id, solicitanteDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Solicitante", description = "Método para remover frases de Solicitante selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Solicitante removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Solicitante selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Solicitantes", description = "Método para Listar todas as frases de Solicitantes cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Solicitantes carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Solicitantes"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<SolicitantePageDTO> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(page, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/lista")
    @Operation(summary = "Lista de solicitantes", description = "Método para carregar uma lista de solicitantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitantes carregados com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da lista de solicitantes"),
            @ApiResponse(responseCode = "500", description = "Falha no servidor")
    })
    public ResponseEntity<List<SolicitanteDTO>> findAll() throws RecordNotFoundException {
        return ResponseEntity.ok(service.findAllSolicitantes());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Solicitante por ID", description = "Método para buscar a frase de Solicitante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Solicitante localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Solicitante selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<SolicitanteDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}

package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.ResumoDTO;
import br.com.laudos.dto.pages.ResumoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.ResumoService;
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
@RequestMapping("/api/resumos")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Resumos Clínicos", description = "Método para salvar, editar, listar e remover dados de frases de Resumos Clínicos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ResumoController {

    private final ResumoService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Resumo Clínico", description = "Método para salvar a frase de Resumo Clínico preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Resumo Clínico salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Resumo Clínico já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<ResumoDTO> salvar(@RequestBody @Valid @NotNull ResumoDTO resumoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(resumoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Resumo Clínico", description = "Método para atualizar a frase de Resumo Clínico selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Resumo Clínico atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Resumo Clínico selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<ResumoDTO> update(@PathVariable @NotNull @Positive Integer id,
                            @RequestBody @Valid @NotNull ResumoDTO resumoDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(id, resumoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Resumo Clínico", description = "Método para remover frases de Resumo Clínico selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Resumo Clínico removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Resumo Clínico selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        this.service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN' or hasRole('MEDICO'))")
    @GetMapping
    @Operation(summary = "Listar frases de Resumos Clínicos", description = "Método para Listar todas as frases de Resumos Clínicos cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Resumos Clínicos carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Resumos Clínicos"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<ResumoPageDTO> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                 @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(page, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/lista")
    @Operation(summary = "Lista de Resumos Clínicos", description = "Método para carregar uma lista de Resumos Clínicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Resumos Clínicos carregados com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da lista de Resumos Clínicos"),
            @ApiResponse(responseCode = "500", description = "Falha no servidor")
    })
    public ResponseEntity<List<ResumoDTO>> findAll() throws RecordNotFoundException {
        return ResponseEntity.ok(service.findAllResumos());
    }

    @PreAuthorize("hasRole('ADMIN' or hasRole('MEDICO'))")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Resumo Clínico por ID", description = "Método para buscar a frase de Resumo Clínico por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Resumo Clínico localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Resumo Clínico selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<ResumoDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
   }
}

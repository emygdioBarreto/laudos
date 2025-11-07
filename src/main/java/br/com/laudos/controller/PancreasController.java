package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.PancreasDTO;
import br.com.laudos.dto.pages.PancreasPageDTO;
import br.com.laudos.service.PancreasService;
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
@RequestMapping("/api/pancreas")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Pancreas", description = "Método para salvar, editar, listar e remover dados de frases de Pancreas")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class PancreasController {

    private final PancreasService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Pancreas", description = "Método para salvar a frase de Pancreas preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Pancreas salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Pancreas já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<PancreasDTO> salvar(@RequestBody @Valid @NotNull PancreasDTO pancreasDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(pancreasDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Pancreas", description = "Método para atualizar a frase de Pancreas selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Pancreas atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Pancreas selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<PancreasDTO> update(
            @PathVariable @NotNull @Positive Integer id,
            @RequestBody @Valid @NotNull PancreasDTO pancreasDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(id, pancreasDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Pancreas", description = "Método para remover frases de Pancreas selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Pancreas removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Pancreas selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Pancreas", description = "Método para Listar todas as frases de Pancreas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Pancreas carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Pancreas"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<PancreasPageDTO> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(page, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Pancreas por ID", description = "Método para buscar a frase de Pancreas por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Pancreas localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Pancreas selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<PancreasDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}

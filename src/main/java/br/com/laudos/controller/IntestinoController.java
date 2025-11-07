package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.IntestinoDTO;
import br.com.laudos.dto.pages.IntestinoPageDTO;
import br.com.laudos.service.IntestinoService;
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
@RequestMapping("/api/intestinos")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Intestino Grosso", description = "Método para salvar, editar, listar e remover dados de frases de Intestino Grosso")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class IntestinoController {

    private final IntestinoService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Intestino Grosso", description = "Metodo para salvar a frase de Intestino Grosso preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Intestino Grosso salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Intestino Grosso já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<IntestinoDTO> salvar(@RequestBody @Valid @NotNull IntestinoDTO intestinoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(intestinoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualização da frase de Intestino Grosso", description = "Método para atualizar a frase de Intestino Grosso selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Intestino Grosso atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Intestino Grosso selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<IntestinoDTO> update(
            @PathVariable @NotNull @Positive Integer id,
            @RequestBody @Valid @NotNull IntestinoDTO intestinoDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(id, intestinoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Intestino Grosso", description = "Método para remover frases de Intestino Grosso selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Intestino Grosso apagada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na deleção da frase de Intestino Grosso selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Intestino Grosso", description = "Método para Listar todas as frases de Intestino Grosso cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Intestino Grosso carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Intestino Grosso"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<IntestinoPageDTO> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(page, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Intestino Grosso por ID", description = "Método para buscar a frase de Intestino Grosso por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Intestino Grosso localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Intestino Grosso selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<IntestinoDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}

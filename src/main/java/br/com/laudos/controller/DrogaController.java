package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.DrogaDTO;
import br.com.laudos.dto.pages.DrogaPageDTO;
import br.com.laudos.service.DrogaService;
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
@RequestMapping("/api/drogas")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Drogas", description = "Método para salvar, editar, listar e remover nomes de drogas utilizadas")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class DrogaController {

    private final DrogaService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar nova droga", description = "Metodo para salvar nova droga utilizada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nome de droga salvo com sucesso!"),
            @ApiResponse(responseCode = "400", description = "No da droga já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<DrogaDTO> salvar(@RequestBody @Valid @NotNull DrogaDTO drogaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(drogaDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualização do nome da droga", description = "Método para atualizar nome de droga selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Nome da droga atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização do nome da droga selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<DrogaDTO> update(@PathVariable @NotNull @Positive Integer id,
                           @RequestBody @Valid @NotNull DrogaDTO drogaDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(id, drogaDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover nome da droga", description = "Método para remover nome de droga selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome da droga removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do nome da droga selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar nomes das drogas ", description = "Método para Listar todos nomes de drogas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de drogas carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de drogas"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<DrogaPageDTO> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int pageSize)  {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(page, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Busca por nome da droga por ID", description = "Método para buscar o nome da droga por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o nome da droga cadastrada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do nome da droga selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<DrogaDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}

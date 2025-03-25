package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.LaudoDTO;
import br.com.laudos.dto.pages.LaudoPageDTO;
import br.com.laudos.service.LaudoService;
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

import java.text.ParseException;

@Validated
@RestController
@RequestMapping("/api/laudos")
@CrossOrigin("*")
@Tag(name = "Laudos", description = "Método para salvar, editar, listar e remover Laudos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class LaudoController {

    private final LaudoService service;

    public LaudoController(LaudoService service) {
        this.service = service;
    }

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
    public ResponseEntity<LaudoDTO> salvar(@RequestBody @Valid @NotNull LaudoDTO laudoDTO) throws ParseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(laudoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Laudo", description = "Método para atualizar o Laudo selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Laudo atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização do Laudo selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LaudoDTO update(@PathVariable @NotNull @Positive Long id,
                           @RequestBody @Valid @NotNull LaudoDTO laudoDTO) {
        return service.update(id, laudoDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover Laudo", description = "Método para remover Laudo selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laudo removido com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do Laudo selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Long id) {
        service.delete(id);
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
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Laudo por ID", description = "Método para buscar Laudo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laudo localizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do Laudo selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LaudoDTO findById(@PathVariable @NotNull @Positive Long id) {
        return service.findById(id);
    }
}

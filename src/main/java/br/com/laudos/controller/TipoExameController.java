package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.TipoExameDTO;
import br.com.laudos.dto.pages.TipoExamePageDTO;
import br.com.laudos.service.TipoExameService;
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
@RequestMapping("/api/tipoexames")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Tipos de Exames", description = "Método para salvar, editar, listar e remover dados de frases de Tipos de Exames")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class TipoExameController {

    private final TipoExameService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Tipo de Exame", description = "Método para salvar a frase de Tipo de Exame preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Tipo de Exame salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Tipo de Exame já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<TipoExameDTO> salvar(@RequestBody @Valid @NotNull TipoExameDTO tipoExameDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(tipoExameDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Tipo de Exame", description = "Método para atualizar a frase de Tipo de Exame selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Tipo de Exame atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Tipo de Exame selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public TipoExameDTO update(@PathVariable @NotNull @Positive Integer id,
                               @RequestBody @Valid @NotNull TipoExameDTO tipoExameDTO ) {
        return service.update(id, tipoExameDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Tipo de Exame", description = "Método para remover frases de Tipo de Exame selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Method... removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Tipo de Exame selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Tipos de Exames", description = "Método para Listar todas as frases de Tipos de Exames cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Tipos de Exames carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Tipos de Exames"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public TipoExamePageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                    @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Tipo de Exame por ID", description = "Método para buscar a frase de Tipo de Exame por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Tipo de Exame localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Tipo de Exame selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public TipoExameDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

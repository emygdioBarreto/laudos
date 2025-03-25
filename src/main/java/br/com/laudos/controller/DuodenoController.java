package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.DuodenoDTO;
import br.com.laudos.dto.pages.DuodenoPageDTO;
import br.com.laudos.service.DuodenoService;
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
@RequestMapping("/api/duodenos")
@CrossOrigin("*")
@Tag(name = "Frases de Duodeno", description = "Método para salvar, editar, listar e remover dados de frases de duodeno")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class DuodenoController {

    private final DuodenoService service;

    public DuodenoController(DuodenoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Duodeno", description = "Metodo para salvar a frase de duodeno preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de duodeno salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de duodeno já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<DuodenoDTO> salvar(@RequestBody @Valid @NotNull DuodenoDTO duodenoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(duodenoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualização da frase de Duodeno", description = "Método para atualizar a frase de duodeno selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de duodeno atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de duodeno selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public DuodenoDTO update(@PathVariable @NotNull @Positive Integer id,
                             @RequestBody @Valid @NotNull DuodenoDTO duodenoDTO) {
        return service.update(id, duodenoDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Duodeno", description = "Método para remover frases de duodeno selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de duodeno apagada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na deleção da frase de duodeno selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Duodeno", description = "Método para Listar todas as frases de duodeno cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de duodeno carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de duodeno"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public DuodenoPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                  @RequestParam(defaultValue = "10") @Positive int pageSize)  {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Busca por frase de Duodeno por ID", description = "Método para buscar a frase de duodeno por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a frase de duodeno com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de duodeno selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public DuodenoDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

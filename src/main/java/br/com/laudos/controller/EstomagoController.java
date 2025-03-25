package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.EstomagoDTO;
import br.com.laudos.dto.pages.EstomagoPageDTO;
import br.com.laudos.service.EstomagoService;
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
import org.springframework.web.bind.annotation.*;

@Valid
@RestController
@RequestMapping("/api/estomagos")
@CrossOrigin("*")
@Tag(name = "Frases de Estômago", description = "Método para salvar, editar, listar e remover dados de frases de Estômago")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class EstomagoController {

    private final EstomagoService service;

    public EstomagoController(EstomagoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Estômago", description = "Metodo para salvar a frase de Estômago preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Estômago salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Estômago já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<EstomagoDTO> salvar(@RequestBody @Valid @NotNull EstomagoDTO estomagoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(estomagoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Estômago", description = "Método para atualizar a frase de Estômago selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Estômago atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Estômago selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public EstomagoDTO update(@PathVariable @NotNull @Positive Integer id,
                              @RequestBody @Valid @NotNull EstomagoDTO estomagoDTO) {
        return service.update(id, estomagoDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Estômago", description = "Método para remover frases de Estômago selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Estômago apagada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na deleção da frase de Estômago selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
       service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Estômago", description = "Método para Listar todas as frases de Estômago cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Estômago carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Estômago"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public EstomagoPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                   @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Estômago por ID", description = "Método para buscar a frase de Estômago por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Estômago localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Estômago selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public EstomagoDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

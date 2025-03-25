package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.ConcluirDTO;
import br.com.laudos.dto.pages.ConcluirPageDTO;
import br.com.laudos.service.ConcluirService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/conclusoes")
@CrossOrigin("*")
@Tag(name = "Frases de Concluir", description = "Método para salvar, editar, listar e remover dados de frases de conclusão")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ConcluirController {

    private final ConcluirService service;

    public ConcluirController(ConcluirService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Conclusão", description = "Método para salvar a frase de conclusão preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de conclusão salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de conclusão já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ConcluirDTO salvar(@RequestBody @Valid @NotNull ConcluirDTO concluirDTO) {
        return service.salvar(concluirDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de conclusão", description = "Método para atualizar a frase de conclusão selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Conclusão atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Conclusão selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ConcluirDTO update(@PathVariable @NotNull @Positive Integer id,
                              @RequestBody @Valid @NotNull ConcluirDTO concluirDTO) {
        return service.update(id, concluirDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover frase de conclusão", description = "Método para remover frases de conclusão selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Conclusão removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Conclusão selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de conclusões", description = "Método para Listar todas as frases de conclusão cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Conclusão carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Conclusão"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ConcluirPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                   @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de conclusão por ID", description = "Método para buscar a frase de conclusão por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de conclusão localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de conclusão selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ConcluirDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

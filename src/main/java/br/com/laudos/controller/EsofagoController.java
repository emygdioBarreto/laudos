package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.EsofagoDTO;
import br.com.laudos.dto.pages.EsofagoPageDTO;
import br.com.laudos.service.EsofagoService;
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
@RequestMapping("/api/esofagos")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Esôfago", description = "Método para salvar, editar, listar e remover dados de frases de Esôfago")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class EsofagoController {

    private final EsofagoService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Esôfago", description = "Metodo para salvar a frase de Esôfago preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Esôfago salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Esôfago já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<EsofagoDTO> salvar(@RequestBody @Valid @NotNull EsofagoDTO esofagoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(esofagoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualização de Esôfago", description = "Método para atualizar a frase de Esôfago selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Esôfago atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Esôfago selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public EsofagoDTO update(@PathVariable @NotNull @Positive Integer id,
                             @RequestBody @Valid @NotNull EsofagoDTO esofagoDTO) {
        return service.update(id, esofagoDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover Esôfago", description = "Método para remover frases de Esôfago selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Esôfago apagada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na deleção da frase de Esôfago selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar Esôfagos", description = "Método para Listar todas as frases de Esôfagos cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Esôfagos carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Esôfagos"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public EsofagoPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                  @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Esôfago por ID", description = "Método para buscar a frase de Esôfago por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Esôfago localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Esôfago selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public EsofagoDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

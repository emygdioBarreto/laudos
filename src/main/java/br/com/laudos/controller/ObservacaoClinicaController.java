package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.ObservacaoClinicaDTO;
import br.com.laudos.dto.pages.ObservacaoClinicaPageDTO;
import br.com.laudos.service.ObservacaoClinicaService;
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
@RequestMapping("/api/observacoesClinicas")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Frases de Observações Clínicas", description = "Método para salvar, editar, listar e remover frases de Observações Clínicas")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ObservacaoClinicaController {

    private final ObservacaoClinicaService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Observação Clínica", description = "Método para salvar a frase de Observação Clínica preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Frase de Observação Clínica salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Frase de Observação Clínica já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<ObservacaoClinicaDTO> salvar(@RequestBody @Valid @NotNull ObservacaoClinicaDTO observacaoClinicaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(observacaoClinicaDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar frase de Observação Clínica", description = "Método para atualizar a frase de Observação Clínica selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Frases de Observação Clínica atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da frase de Observação Clínica selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ObservacaoClinicaDTO update(@PathVariable @NotNull @Positive Integer id,
                                       @RequestBody @Valid @NotNull ObservacaoClinicaDTO observacaoClinicaDTO) {
        return service.update(id, observacaoClinicaDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover frase de Observação Clínica", description = "Método para remover frases de Observação Clínica selecionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frases de Observação Clínica removida com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção da frase de Observação Clínica selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar frases de Observação Clínica", description = "Método para Listar todas as frases de Observação Clínica cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de frases de Observação Clínica carregadas com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga das frases de Observação Clínica"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ObservacaoClinicaPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                            @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar frase de Observação Clínica por ID", description = "Método para buscar a frase de Observação Clínica por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frase de Observação Clínica localizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga da frase de Observação Clínica selecionada"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ObservacaoClinicaDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.MedicoDTO;
import br.com.laudos.dto.pages.MedicoPageDTO;
import br.com.laudos.service.MedicoService;
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
@RequestMapping("/api/medicos")
@CrossOrigin("*")
@Tag(name = "Médico Executor", description = "Método para salvar, editar, listar e remover dados do Médico Executor")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Médico Executor", description = "Método para salvar o Médico Executor preenchido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médico Executor salvo com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Médico Executor já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<MedicoDTO> salvar(@RequestBody @Valid @NotNull MedicoDTO medicoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(medicoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{crm}")
    @Operation(summary = "Atualizaçr Médico Executor", description = "Método para atualizar o Médico Executor selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Médico Executor atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização do Médico Executor selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public MedicoDTO update(@PathVariable @NotNull @Positive String crm,
                            @RequestBody @Valid @NotNull MedicoDTO medicoDTO) {
        return service.update(crm, medicoDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{crm}")
    @Operation(summary = "Remover Médico Executor", description = "Método para remover Médico Executor selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico Executor removido com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do Médico Executor selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull String crm) {
        service.delete(crm);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar Médicos Executores", description = "Método para Listar todas os Médicos Executores cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Médicos Executores carregados com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga dos Médicos Executores"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public MedicoPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                     @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{crm}")
    @Operation(summary = "Buscar Médico Executor por CRM", description = "Método para buscar o Médico Executor por CRM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico Executor localizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do Médico Executor selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public MedicoDTO findById(@PathVariable @NotNull String crm) {
        return service.findById(crm);
    }
}

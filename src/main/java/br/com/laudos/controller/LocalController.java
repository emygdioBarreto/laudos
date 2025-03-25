package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.LocalDTO;
import br.com.laudos.dto.pages.LocalPageDTO;
import br.com.laudos.service.LocalService;
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
@RequestMapping("/api/locais")
@CrossOrigin("*")
@Tag(name = "Local", description = "Método para salvar, editar, listar e remover dados do local de realização do exame")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class LocalController {

    private final LocalService service;

    public LocalController(LocalService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Local", description = "Método para salvar o Local preenchido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Local salvo com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Local já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<LocalDTO> salvar(@RequestBody @Valid @NotNull LocalDTO localDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(localDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Local", description = "Método para atualizar o Local selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Local atualizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização do Local selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LocalDTO update(@PathVariable @NotNull @Positive Integer id,
                           @RequestBody @Valid @NotNull LocalDTO localDTO) {
        return service.update(id, localDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover Local", description = "Método para remover Local selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Local removido com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do Local selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar Locais", description = "Método para Listar todas os Locais cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Locais carregados com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga dos Locais de realização de exames"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LocalPageDTO findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return service.findAll(page, pageSize);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Local por ID", description = "Método para buscar Local por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Local localizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do Local selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public LocalDTO findById(@PathVariable @NotNull @Positive Integer id) {
        return service.findById(id);
    }
}

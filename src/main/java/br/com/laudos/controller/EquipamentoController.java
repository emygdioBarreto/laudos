package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.EquipamentoDTO;
import br.com.laudos.dto.pages.EquipamentoPageDTO;
import br.com.laudos.service.EquipamentoService;
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
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Descrição de Equipamento", description = "Método para salvar, editar, listar e remover dados de Equipamentos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class EquipamentoController {

    private final EquipamentoService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping("/save")
    @Operation(summary = "Salvar Equipamento", description = "Metodo para salvar a descrição de equipamento preenchida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Descrição de equipamento salva com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Descrição de equipamento já existe na base de dados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<EquipamentoDTO> salvar(@RequestBody @Valid @NotNull EquipamentoDTO equipamentoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(equipamentoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualização da descrição de Equipamento", description = "Método para atualizar a descrição do equipamento selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Descrição de equipamento atualizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na atualização da descrição de equipamento selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<EquipamentoDTO> update(@PathVariable @NotNull @Positive Integer id,
                                 @RequestBody @Valid @NotNull EquipamentoDTO equipamentoDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(id, equipamentoDTO));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover descrição de Equipamento", description = "Método para remover o equipamento selecionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamento removido com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do equipamento selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Integer id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Listar Equipamentos", description = "Método para Listar todos os equipamentos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipamentos carregados com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga dos equipamentos cadastrados"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<EquipamentoPageDTO> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                      @RequestParam(defaultValue = "10") @Positive int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(page, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(summary = "Busca Equipamento por ID", description = "Método para buscar equipamento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipamento localizado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do equipamento selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public ResponseEntity<EquipamentoDTO> findById(@PathVariable @NotNull @Positive Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}

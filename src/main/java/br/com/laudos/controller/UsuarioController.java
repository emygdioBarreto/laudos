package br.com.laudos.controller;

import br.com.laudos.config.SecurityConfig;
import br.com.laudos.dto.UsuarioDTO;
import br.com.laudos.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "usuario", description = "Método para criação, manutenção e remoção de um usuário no sistema")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class UsuarioController {

    private final UsuarioService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PostMapping
    @Operation(summary = "Salvar usuário", description = "Método para salvar um usuário na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário salvo com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na gravação de um usuário"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Falha no servidor")
    })
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody UsuarioDTO userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(userDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Método para atualizar os dados de um usuário na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na gravação de um usuário"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Falha no servidor")
    })
    public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioDTO userDto) {
        return service.update(id, userDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover usuário", description = "Método para remover um usuário da base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na remoção do usuário selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public void delete(@PathVariable @NotNull @Positive Long id) {
        service.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping
    @Operation(summary = "Lista de usuários", description = "Método para carregar a lista de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados dos usuários carregados com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga dos dados dos usuários"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Falha no servidor")
    })
    public List<UsuarioDTO> findAll(){
        return service.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    @GetMapping("/{id}")
    @Operation(description = "Busca usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recupera usuário por ID com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Ocorreu uma falha na carga do usuário selecionado"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    public UsuarioDTO findById(@PathVariable @NotNull @Positive Long id) {
        return service.findById(id);
    }
}

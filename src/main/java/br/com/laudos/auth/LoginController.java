package br.com.laudos.auth;

import br.com.laudos.config.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
@CrossOrigin(origins = "*")
@Tag(name = "login", description = "Efetuar o login do usuário no sistema de Laudos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    @Operation(summary = "Login no sistema", description = "Metodo para autenticar o usuário e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso!"),
            @ApiResponse(responseCode = "401", description = "Login ou senha inválidos"),
            @ApiResponse(responseCode = "403", description = "Login não autorizado")
    })
    public ResponseEntity<String> logar(@RequestBody Login login) {
        try {
            return ResponseEntity.ok(this.loginService.logar(login));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }
}

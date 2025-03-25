package br.com.laudos.auth;

import br.com.laudos.config.JwtServiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LoginRepository repository;
    private final JwtServiceGenerator jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(LoginRepository repository, JwtServiceGenerator jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String logar(Login login) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),
                        login.getPassword()
                )
        );
        Usuario user = repository.findByUsername(login.getUsername());
        return jwtService.generateToken(user);
    }
}

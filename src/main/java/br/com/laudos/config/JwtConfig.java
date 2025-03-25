package br.com.laudos.config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class JwtConfig {

    JwtConfig() {
    }

    //Parâmetros para geração do Token
    public static final String SECRECT_KEY = "UMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUIUMACHAVESECRETADASUAAPIAQUI";
    public static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;
    public static final int HORAS_EXPIRACAO_TOKEN = 8;
}

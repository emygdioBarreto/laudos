package br.com.laudos.stepDomain;

import br.com.laudos.LaudosApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = LaudosApplication.class)
public class CucumberDefsDefault {
}

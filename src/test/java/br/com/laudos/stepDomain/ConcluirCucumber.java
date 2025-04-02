package br.com.laudos.stepDomain;

import br.com.laudos.domain.Concluir;
import br.com.laudos.repository.ConcluirRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ConcluirCucumber extends CucumberDefsDefault {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ConcluirRepository repository;

    private ResponseEntity<Concluir[]> response;

    private final List<Concluir> concluirCadastradas = new ArrayList<>();

    @Dado("que existam frases de concluir cadastradas no sistema:")
    public void queExistamFrasesDeConcluirCadastradasNoSistema(DataTable dataTable) {
        List<Map<String, String>> conclusoes = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> concluirData : conclusoes) {
            Concluir concluir = new Concluir();
            concluir.setId(Integer.valueOf(concluirData.get("id")));
            concluir.setConclusao(concluirData.get("conclusao"));
            repository.save(concluir);
            concluirCadastradas.add(concluir);
        }
    }

    @Quando("eu faço uma requisição GET para obter as frases de concluir")
    public void euFaçoUmaRequisiçãoGETParaObterAsFrasesDeConcluir() throws URISyntaxException {
        response = testRestTemplate.getForEntity(new URI("/api/conclusoes"), Concluir[].class);
    }

    @Então("a resposta deve ter o status code {int}")
    public void aRespostaDeveTerOStatusCode200(int statusCode) {
        assertThat(200).isEqualTo(statusCode);
    }

    @E("a resposta deve conter os dados que foram cadastrados previamente")
    public void aRespostaDeveConterOsDadosQueForamCadastradosPreviamente() {
        concluirCadastradas.forEach(concluir -> {
            assertTrue(concluirCadastradas.contains(concluir));
        });
    }
}

package br.com.laudos.stepDomain;

import br.com.laudos.domain.Concluir;
import br.com.laudos.repository.ConcluirRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ConcluirCucumber extends CucumberDefsDefault {

    private static final int STATUS_CODE_EXPECT = 200;

    private final TestRestTemplate testRestTemplate;

    private final ConcluirRepository repository;

    private final List<Concluir> concluirCadastradas = new ArrayList<>();

    public ConcluirCucumber(TestRestTemplate testRestTemplate, ConcluirRepository repository) {
        this.testRestTemplate = testRestTemplate;
        this.repository = repository;
    }

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

    @Quando("eu faco uma requisicao GET para obter as frases de concluir")
    public void euFacoUmaRequisicaoGETParaObterAsFrasesDeConcluir() throws URISyntaxException {
        testRestTemplate.getForEntity(new URI("/api/conclusoes"), Concluir[].class);
    }

    @Entao("a resposta deve ter o status code {int}")
    public void aRespostaDeveTerOStatusCode200(int statusCode) {
        assertThat(statusCode).isEqualTo(STATUS_CODE_EXPECT);
    }

    @E("a resposta deve conter os dados que foram cadastrados previamente")
    public void aRespostaDeveConterOsDadosQueForamCadastradosPreviamente() {
        concluirCadastradas.forEach(concluir -> assertTrue(concluirCadastradas.contains(concluir)));
    }
}

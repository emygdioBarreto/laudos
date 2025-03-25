package br.com.laudos.repository;

import br.com.laudos.domain.Premedicacao;
import br.com.laudos.service.PremedicacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremedicacaoRepositoryTest {

    @InjectMocks
    private PremedicacaoService service;

    @Mock
    private PremedicacaoRepository repository;

    private List<Premedicacao> premedicacoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Premedicacao premedicacao1 = new Premedicacao(1, "Xylocaína tópica à 10%.");
        Premedicacao premedicacao2 = new Premedicacao(2, "Xylocaína tópica à 15%.");
        premedicacoes = List.of(premedicacao1, premedicacao2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de premedicação ordenadas por ID")
    void buscarTodasFrasesPremedicacaoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(premedicacoes);

        List<Premedicacao> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(premedicacoes.getClass(), response.getClass());
        assertEquals(premedicacoes.size(), response.size());

        assertThat(response).containsAll(premedicacoes);

        assertEquals(premedicacoes.get(0), response.get(0));
        assertEquals(premedicacoes.get(0).getId(), response.get(0).getId());
        assertEquals(premedicacoes.get(0).getAnalgesia(), response.get(0).getAnalgesia());

        assertEquals(premedicacoes.get(1), response.get(1));
        assertEquals(premedicacoes.get(1).getId(), response.get(1).getId());
        assertEquals(premedicacoes.get(1).getAnalgesia(), response.get(1).getAnalgesia());
    }
}
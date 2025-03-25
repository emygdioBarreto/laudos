package br.com.laudos.repository;

import br.com.laudos.domain.Observacao;
import br.com.laudos.service.ObservacaoService;
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
class ObservacaoRepositoryTest {

    @InjectMocks
    private ObservacaoService service;

    @Mock
    private ObservacaoRepository repository;

    private List<Observacao> observacoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Observacao observacao1 = new Observacao(1, "Exame normal do ponto de vista endoscópico.");
        Observacao observacao2 = new Observacao(2, "Exame normal do ponto de vista endoscópico 2.");
        observacoes = List.of(observacao1, observacao2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de intestino ordenadas por ID")
    void buscarTodasFrasesIntestinoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(observacoes);

        List<Observacao> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(observacoes.getClass(), response.getClass());
        assertEquals(observacoes.size(), response.size());

        assertThat(response).containsAll(observacoes);

        assertEquals(observacoes.get(0), response.get(0));
        assertEquals(observacoes.get(0).getId(), response.get(0).getId());
        assertEquals(observacoes.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(observacoes.get(1), response.get(1));
        assertEquals(observacoes.get(1).getId(), response.get(1).getId());
        assertEquals(observacoes.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
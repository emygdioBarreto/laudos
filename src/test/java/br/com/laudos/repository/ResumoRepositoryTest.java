package br.com.laudos.repository;

import br.com.laudos.domain.Resumo;
import br.com.laudos.service.ResumoService;
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
class ResumoRepositoryTest {

    @InjectMocks
    private ResumoService service;

    @Mock
    private ResumoRepository repository;

    private List<Resumo> resumos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Resumo resumo1 = new Resumo(1, "Exame normal do ponto de vista endoscópico.");
        Resumo resumo2 = new Resumo(2, "Exame normal do ponto de vista endoscópico 2.");
        resumos = List.of(resumo1, resumo2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de intestino ordenadas por ID")
    void buscarTodasFrasesIntestinoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(resumos);

        List<Resumo> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(resumos.getClass(), response.getClass());
        assertEquals(resumos.size(), response.size());

        assertThat(response).containsAll(resumos);

        assertEquals(resumos.get(0), response.get(0));
        assertEquals(resumos.get(0).getId(), response.get(0).getId());
        assertEquals(resumos.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(resumos.get(1), response.get(1));
        assertEquals(resumos.get(1).getId(), response.get(1).getId());
        assertEquals(resumos.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
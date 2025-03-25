package br.com.laudos.repository;

import br.com.laudos.domain.Pancreas;
import br.com.laudos.service.PancreasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PancreasRepositoryTest {

    @InjectMocks
    private PancreasService service;

    @Mock
    private PancreasRepository repository;

    private List<Pancreas> pancreass;

    @BeforeEach
    void setUp() {
        Pancreas intestino1 = new Pancreas(1, "Exame normal do ponto de vista endoscópico.");
        Pancreas intestino2 = new Pancreas(2, "Exame normal do ponto de vista endoscópico 2.");
        pancreass = List.of(intestino1, intestino2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de pancreas ordenadas por ID")
    void buscarTodasFrasesPancreasOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(pancreass);

        List<Pancreas> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(pancreass.getClass(), response.getClass());
        assertEquals(pancreass.size(), response.size());

        assertThat(response).containsAll(pancreass);

        assertEquals(pancreass.get(0), response.get(0));
        assertEquals(pancreass.get(0).getId(), response.get(0).getId());
        assertEquals(pancreass.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(pancreass.get(1), response.get(1));
        assertEquals(pancreass.get(1).getId(), response.get(1).getId());
        assertEquals(pancreass.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
package br.com.laudos.repository;

import br.com.laudos.domain.Pancrea;
import br.com.laudos.service.PancreaService;
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
class PancreaRepositoryTest {

    @InjectMocks
    private PancreaService service;

    @Mock
    private PancreaRepository repository;

    private List<Pancrea> pancreas;

    @BeforeEach
    void setUp() {
        Pancrea intestino1 = new Pancrea(1, "Exame normal do ponto de vista endoscópico.");
        Pancrea intestino2 = new Pancrea(2, "Exame normal do ponto de vista endoscópico 2.");
        pancreas = List.of(intestino1, intestino2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de pancreas ordenadas por ID")
    void buscarTodasFrasesPancreasOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(pancreas);

        List<Pancrea> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(pancreas.getClass(), response.getClass());
        assertEquals(pancreas.size(), response.size());

        assertThat(response).containsAll(pancreas);

        assertEquals(pancreas.get(0), response.get(0));
        assertEquals(pancreas.get(0).getId(), response.get(0).getId());
        assertEquals(pancreas.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(pancreas.get(1), response.get(1));
        assertEquals(pancreas.get(1).getId(), response.get(1).getId());
        assertEquals(pancreas.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
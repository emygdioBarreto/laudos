package br.com.laudos.repository;

import br.com.laudos.domain.Estomago;
import br.com.laudos.service.EstomagoService;
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
class EstomagoRepositoryTest {

    @InjectMocks
    private EstomagoService service;

    @Mock
    private EstomagoRepository repository;

    private List<Estomago> estomagos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Estomago estomago1 = new Estomago(1, "Pequena quantidade de líquido de estase claro");
        Estomago estomago2 = new Estomago(2, "Pequena quantidade de líquido de estase claro 2.");
        estomagos = List.of(estomago1, estomago2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de estomago ordenadas por ID")
    void buscarTodasFrasesEstomagoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(estomagos);

        List<Estomago> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(estomagos.getClass(), response.getClass());
        assertEquals(estomagos.size(), response.size());

        assertThat(response).containsAll(estomagos);

        assertEquals(estomagos.get(0), response.get(0));
        assertEquals(estomagos.get(0).getId(), response.get(0).getId());
        assertEquals(estomagos.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(estomagos.get(1), response.get(1));
        assertEquals(estomagos.get(1).getId(), response.get(1).getId());
        assertEquals(estomagos.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
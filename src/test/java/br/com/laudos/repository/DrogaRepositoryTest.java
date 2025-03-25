package br.com.laudos.repository;

import br.com.laudos.domain.Droga;
import br.com.laudos.service.DrogaService;
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
class DrogaRepositoryTest {

    @InjectMocks
    private DrogaService service;

    @Mock
    private DrogaRepository repository;

    private List<Droga> drogas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Droga droga1 = new Droga(1, "Teste de gravação de frase de Duodeno");
        Droga droga2 = new Droga(2, "Teste de gravação de frase de Duodeno 2.");
        drogas = List.of(droga1, droga2);
    }

    @Test
    @DisplayName(value = "Buscar todas as drogas ordenadas por ID")
    void buscarTodasDrogasOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(drogas);

        List<Droga> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(drogas.getClass(), response.getClass());
        assertEquals(drogas.size(), response.size());

        assertThat(response).containsAll(drogas);

        assertEquals(drogas.get(0), response.get(0));
        assertEquals(drogas.get(0).getId(), response.get(0).getId());
        assertEquals(drogas.get(0).getNomedroga(), response.get(0).getNomedroga());

        assertEquals(drogas.get(1), response.get(1));
        assertEquals(drogas.get(1).getId(), response.get(1).getId());
        assertEquals(drogas.get(1).getNomedroga(), response.get(1).getNomedroga());
    }
}
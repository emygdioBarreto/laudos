package br.com.laudos.repository;

import br.com.laudos.domain.Esofago;
import br.com.laudos.service.EsofagoService;
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
class EsofagoRepositoryTest {

    @InjectMocks
    private EsofagoService service;

    @Mock
    private EsofagoRepository repository;

    private List<Esofago> esofagos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Esofago esofago1 = new Esofago(1, "Teste de gravação de frase de Esôfago");
        Esofago esofago2 = new Esofago(2, "Morfologia conservada");
        esofagos = List.of(esofago1, esofago2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de esofagos ordenadas por ID")
    void buscarTodasFrasesEsofagosOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(esofagos);

        List<Esofago> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(esofagos.getClass(), response.getClass());
        assertEquals(esofagos.size(), response.size());

        assertThat(response).containsAll(esofagos);

        assertEquals(esofagos.get(0), response.get(0));
        assertEquals(esofagos.get(0).getId(), response.get(0).getId());
        assertEquals(esofagos.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(esofagos.get(1), response.get(1));
        assertEquals(esofagos.get(1).getId(), response.get(1).getId());
        assertEquals(esofagos.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
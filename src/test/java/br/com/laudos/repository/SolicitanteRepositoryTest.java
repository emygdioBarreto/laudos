package br.com.laudos.repository;

import br.com.laudos.domain.Solicitante;
import br.com.laudos.service.SolicitanteService;
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
class SolicitanteRepositoryTest {

    @InjectMocks
    private SolicitanteService service;

    @Mock
    private SolicitanteRepository repository;

    private List<Solicitante> solicitantes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Solicitante solicitante1 = new Solicitante(1, "Solicitante 1");
        Solicitante solicitante2 = new Solicitante(2, "Solicitante 2");
        solicitantes = List.of(solicitante1, solicitante2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de intestino ordenadas por ID")
    void buscarTodasFrasesIntestinoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(solicitantes);

        List<Solicitante> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(solicitantes.getClass(), response.getClass());
        assertEquals(solicitantes.size(), response.size());

        assertThat(response).containsAll(solicitantes);

        assertEquals(solicitantes.get(0), response.get(0));
        assertEquals(solicitantes.get(0).getId(), response.get(0).getId());
        assertEquals(solicitantes.get(0).getMedicoSolicitante(), response.get(0).getMedicoSolicitante());

        assertEquals(solicitantes.get(1), response.get(1));
        assertEquals(solicitantes.get(1).getId(), response.get(1).getId());
        assertEquals(solicitantes.get(1).getMedicoSolicitante(), response.get(1).getMedicoSolicitante());
    }
}
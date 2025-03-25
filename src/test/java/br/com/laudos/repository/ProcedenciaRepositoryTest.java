package br.com.laudos.repository;

import br.com.laudos.domain.Procedencia;
import br.com.laudos.service.ProcedenciaService;
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
class ProcedenciaRepositoryTest {

    @InjectMocks
    private ProcedenciaService service;

    @Mock
    private ProcedenciaRepository repository;

    private List<Procedencia> procedencias;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Procedencia procedencia1 = new Procedencia(1, "Hospital");
        Procedencia procedencia2 = new Procedencia(2, "Pronto Socorro");
        procedencias = List.of(procedencia1, procedencia2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de intestino ordenadas por ID")
    void buscarTodasFrasesIntestinoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(procedencias);

        List<Procedencia> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(procedencias.getClass(), response.getClass());
        assertEquals(procedencias.size(), response.size());

        assertThat(response).containsAll(procedencias);

        assertEquals(procedencias.get(0), response.get(0));
        assertEquals(procedencias.get(0).getId(), response.get(0).getId());
        assertEquals(procedencias.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(procedencias.get(1), response.get(1));
        assertEquals(procedencias.get(1).getId(), response.get(1).getId());
        assertEquals(procedencias.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
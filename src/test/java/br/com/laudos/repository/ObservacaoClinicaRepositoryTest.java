package br.com.laudos.repository;

import br.com.laudos.domain.ObservacaoClinica;
import br.com.laudos.service.ObservacaoClinicaService;
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
class ObservacaoClinicaRepositoryTest {

    @InjectMocks
    private ObservacaoClinicaService service;

    @Mock
    private ObservacaoClinicaRepository repository;

    private List<ObservacaoClinica> observacaoClinicas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ObservacaoClinica observacaoClinica1 = new ObservacaoClinica(1, "Exame normal do ponto de vista endoscópico.");
        ObservacaoClinica observacaoClinica2 = new ObservacaoClinica(2, "Exame normal do ponto de vista endoscópico 2.");
        observacaoClinicas = List.of(observacaoClinica1, observacaoClinica2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de intestino ordenadas por ID")
    void buscarTodasFrasesIntestinoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(observacaoClinicas);

        List<ObservacaoClinica> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(observacaoClinicas.getClass(), response.getClass());
        assertEquals(observacaoClinicas.size(), response.size());

        assertThat(response).containsAll(observacaoClinicas);

        assertEquals(observacaoClinicas.get(0), response.get(0));
        assertEquals(observacaoClinicas.get(0).getId(), response.get(0).getId());
        assertEquals(observacaoClinicas.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(observacaoClinicas.get(1), response.get(1));
        assertEquals(observacaoClinicas.get(1).getId(), response.get(1).getId());
        assertEquals(observacaoClinicas.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
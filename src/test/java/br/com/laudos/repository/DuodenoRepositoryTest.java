package br.com.laudos.repository;

import br.com.laudos.domain.Duodeno;
import br.com.laudos.service.DuodenoService;
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
class DuodenoRepositoryTest {

    @InjectMocks
    private DuodenoService service;

    @Mock
    private DuodenoRepository repository;

    private List<Duodeno> duodenos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Duodeno duodeno1 = new Duodeno(1, "Teste de gravação de frase de Duodeno");
        Duodeno duodeno2 = new Duodeno(2, "Papila anatômica");
        duodenos = List.of(duodeno1, duodeno2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de duodeno ordenadas por ID")
    void buscarTodasFrasesDuodenosOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(duodenos);

        List<Duodeno> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(duodenos.getClass(), response.getClass());
        assertEquals(duodenos.size(), response.size());

        assertThat(response).containsAll(duodenos);

        assertEquals(duodenos.get(0), response.get(0));
        assertEquals(duodenos.get(0).getId(), response.get(0).getId());
        assertEquals(duodenos.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(duodenos.get(1), response.get(1));
        assertEquals(duodenos.get(1).getId(), response.get(1).getId());
        assertEquals(duodenos.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
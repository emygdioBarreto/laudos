package br.com.laudos.repository;

import br.com.laudos.domain.Intestino;
import br.com.laudos.service.IntestinoService;
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
class IntestinoRepositoryTest {

    @InjectMocks
    private IntestinoService service;

    @Mock
    private IntestinoRepository repository;

    private List<Intestino> intestinos;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Intestino intestino1 = new Intestino(1, "Exame normal do ponto de vista endoscópico.");
        Intestino intestino2 = new Intestino(2, "Exame normal do ponto de vista endoscópico 2.");
        intestinos = List.of(intestino1, intestino2);
    }

    @Test
    @DisplayName(value = "Buscar todas as frases de intestino ordenadas por ID")
    void buscarTodasFrasesIntestinoOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(intestinos);

        List<Intestino> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(intestinos.getClass(), response.getClass());
        assertEquals(intestinos.size(), response.size());

        assertThat(response).containsAll(intestinos);

        assertEquals(intestinos.get(0), response.get(0));
        assertEquals(intestinos.get(0).getId(), response.get(0).getId());
        assertEquals(intestinos.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(intestinos.get(1), response.get(1));
        assertEquals(intestinos.get(1).getId(), response.get(1).getId());
        assertEquals(intestinos.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
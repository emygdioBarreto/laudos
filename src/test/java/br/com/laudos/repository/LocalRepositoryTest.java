package br.com.laudos.repository;

import br.com.laudos.domain.Local;
import br.com.laudos.service.LocalService;
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
class LocalRepositoryTest {

    @InjectMocks
    private LocalService service;

    @Mock
    private LocalRepository repository;

    private List<Local> locais;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Local local1 = new Local(1, "Boris Berenstein Imagem e Laboratório");
        Local local2 = new Local(2, "Boris Berenstein Imagem e Laboratório 2");
        locais = List.of(local1, local2);
    }

    @Test
    @DisplayName(value = "Buscar todos os locais ordenadas por ID")
    void buscarTodosLocaisOrdenadasPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(locais);

        List<Local> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(locais.getClass(), response.getClass());
        assertEquals(locais.size(), response.size());

        assertThat(response).containsAll(locais);

        assertEquals(locais.get(0), response.get(0));
        assertEquals(locais.get(0).getId(), response.get(0).getId());
        assertEquals(locais.get(0).getDescricao(), response.get(0).getDescricao());

        assertEquals(locais.get(1), response.get(1));
        assertEquals(locais.get(1).getId(), response.get(1).getId());
        assertEquals(locais.get(1).getDescricao(), response.get(1).getDescricao());
    }
}
package br.com.laudos.repository;

import br.com.laudos.domain.TipoExame;
import br.com.laudos.service.TipoExameService;
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
class TipoExameRepositoryTest {

    @InjectMocks
    private TipoExameService service;

    @Mock
    private TipoExameRepository repository;

    private List<TipoExame> tipoExames;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        TipoExame tipoExame1 = new TipoExame(1, "Endoscopia Digestiva Alta", "C", true, true,true, false, false);
        TipoExame tipoExame2 = new TipoExame(6, "Endoscopia Digestiva Alta + Polipectomia", "C", true, true,true, false, false);
        tipoExames = List.of(tipoExame1, tipoExame2);
    }

    @Test
    @DisplayName(value = "Buscar todos os tipos de exames ordenados por ID")
    void buscarTodosTiposExamesOrdenadosPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(tipoExames);

        List<TipoExame> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(tipoExames.getClass(), response.getClass());
        assertEquals(tipoExames.size(), response.size());

        assertThat(response).containsAll(tipoExames);

        assertEquals(tipoExames.get(0), response.get(0));
        assertEquals(tipoExames.get(0).getId(), response.get(0).getId());
        assertEquals(tipoExames.get(0).getDescricao(), response.get(0).getDescricao());
        assertEquals(tipoExames.get(0).getOrdena(), response.get(0).getOrdena());
        assertEquals(tipoExames.get(0).isEsofago(), response.get(0).isEsofago());
        assertEquals(tipoExames.get(0).isEstomago(), response.get(0).isEstomago());
        assertEquals(tipoExames.get(0).isDuodeno(), response.get(0).isDuodeno());
        assertEquals(tipoExames.get(0).isIntestino(), response.get(0).isIntestino());
        assertEquals(tipoExames.get(0).isPancreas(), response.get(0).isPancreas());

        assertEquals(tipoExames.get(1), response.get(1));
        assertEquals(tipoExames.get(1).getId(), response.get(1).getId());
        assertEquals(tipoExames.get(1).getDescricao(), response.get(1).getDescricao());
        assertEquals(tipoExames.get(0).getOrdena(), response.get(0).getOrdena());
        assertEquals(tipoExames.get(0).isEsofago(), response.get(0).isEsofago());
        assertEquals(tipoExames.get(0).isEstomago(), response.get(0).isEstomago());
        assertEquals(tipoExames.get(0).isDuodeno(), response.get(0).isDuodeno());
        assertEquals(tipoExames.get(0).isIntestino(), response.get(0).isIntestino());
        assertEquals(tipoExames.get(0).isPancreas(), response.get(0).isPancreas());
    }
}
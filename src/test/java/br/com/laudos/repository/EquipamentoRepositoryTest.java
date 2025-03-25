package br.com.laudos.repository;

import br.com.laudos.domain.Equipamento;
import br.com.laudos.service.EquipamentoService;
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
class EquipamentoRepositoryTest {

    @InjectMocks
    private EquipamentoService service;

    @Mock
    private EquipamentoRepository repository;

    private List<Equipamento> equipamentos;
    @BeforeEach

    void setUp() {
        MockitoAnnotations.openMocks(this);

        Equipamento equip1 = new Equipamento(1, "Pentax EG290I", "30", "10", "10", "10", "1", "Recife", "C");
        Equipamento equip2 = new Equipamento(2, "Pentax EG290I", "30", "10", "10", "10", "1", "Recife", "C");
        equipamentos = List.of(equip1, equip2);
    }

    @Test
    @DisplayName(value = "Buscar todos os equipamentos ordenados por ID")
    void buscarTodosEquipamentosOrdenadosPorId() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(equipamentos);

        List<Equipamento> response = repository.findAllByOrderByIdAsc();

        assertNotNull(response);
        assertEquals(equipamentos.getClass(), response.getClass());
        assertEquals(equipamentos.size(), response.size());

        assertThat(response).containsAll(equipamentos);

        assertEquals(equipamentos.get(0), response.get(0));
        assertEquals(equipamentos.get(0).getId(), response.get(0).getId());
        assertEquals(equipamentos.get(0).getDescricao(), response.get(0).getDescricao());
        assertEquals(equipamentos.get(0).getSuperior(), response.get(0).getSuperior());
        assertEquals(equipamentos.get(0).getInferior(), response.get(0).getInferior());
        assertEquals(equipamentos.get(0).getDireita(), response.get(0).getDireita());
        assertEquals(equipamentos.get(0).getEsquerda(), response.get(0).getEsquerda());
        assertEquals(equipamentos.get(0).getModLaudo(), response.get(0).getModLaudo());
        assertEquals(equipamentos.get(0).getCidade(), response.get(0).getCidade());
        assertEquals(equipamentos.get(0).getOrdena(), response.get(0).getOrdena());

        assertEquals(equipamentos.get(1), response.get(1));
        assertEquals(equipamentos.get(1).getId(), response.get(1).getId());
        assertEquals(equipamentos.get(1).getDescricao(), response.get(1).getDescricao());
        assertEquals(equipamentos.get(1).getSuperior(), response.get(1).getSuperior());
        assertEquals(equipamentos.get(1).getInferior(), response.get(1).getInferior());
        assertEquals(equipamentos.get(1).getDireita(), response.get(1).getDireita());
        assertEquals(equipamentos.get(1).getEsquerda(), response.get(1).getEsquerda());
        assertEquals(equipamentos.get(1).getModLaudo(), response.get(1).getModLaudo());
        assertEquals(equipamentos.get(1).getCidade(), response.get(1).getCidade());
        assertEquals(equipamentos.get(1).getOrdena(), response.get(1).getOrdena());
    }
}
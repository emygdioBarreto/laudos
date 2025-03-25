package br.com.laudos.repository;

import br.com.laudos.domain.Medico;
import br.com.laudos.service.MedicoService;
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
class MedicoRepositoryTest {

    @InjectMocks
    private MedicoService service;

    @Mock
    private MedicoRepository repository;

    private List<Medico> medicos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Medico medico1 = new Medico("1111", "Dr. João da Silva");
        Medico medico2 = new Medico("1112", "Dra. Maria da Silva");
        medicos = List.of(medico1, medico2);
    }

    @Test
    @DisplayName(value = "Buscar todos os médicos executores ordenados por ID")
    void buscarTodosMedicosExecutoresOrdenadosPorId() {
        when(repository.findAllByOrderByCrmAsc()).thenReturn(medicos);

        List<Medico> response = repository.findAllByOrderByCrmAsc();

        assertNotNull(response);
        assertEquals(medicos.getClass(), response.getClass());
        assertEquals(medicos.size(), response.size());

        assertThat(response).containsAll(medicos);

        assertEquals(medicos.get(0), response.get(0));
        assertEquals(medicos.get(0).getCrm(), response.get(0).getCrm());
        assertEquals(medicos.get(0).getMedicoExecutor(), response.get(0).getMedicoExecutor());

        assertEquals(medicos.get(1), response.get(1));
        assertEquals(medicos.get(1).getCrm(), response.get(1).getCrm());
        assertEquals(medicos.get(1).getMedicoExecutor(), response.get(1).getMedicoExecutor());
    }
}
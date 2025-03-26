package br.com.laudos.controller;

import br.com.laudos.domain.Equipamento;
import br.com.laudos.dto.EquipamentoDTO;
import br.com.laudos.dto.mapper.EquipamentoMapper;
import br.com.laudos.dto.pages.EquipamentoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.EquipamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipamentoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de conclusão não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private EquipamentoController controller;

    @Mock
    private EquipamentoService service;

    @Mock
    private EquipamentoMapper mapper;

    private Equipamento equipamento;
    private EquipamentoDTO equipamentoDTO;
    private Equipamento equipamento1;
    private EquipamentoDTO equipamento1DTO;

    private final List<EquipamentoDTO> equipamentos = new ArrayList<>();
    private EquipamentoPageDTO equipamentoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipamento = new Equipamento(null, "Teste de Gravação de equipamento", "30", "10", "10", "10", "1", "Recife", "C");
        equipamentoDTO = new EquipamentoDTO(null, "Teste de Gravação de equipamento", "30", "10", "10", "10", "1", "Recife", "C");
        equipamento1 = new Equipamento(1, "Pentax EG290I", "30", "10", "10", "10", "1", "Recife", "C");
        equipamento1DTO = new EquipamentoDTO(1, "Pentax EG290I Teste", "30", "10", "10", "10", "1", "Recife", "C");

        equipamentos.add(equipamentoDTO);
        equipamentoPageDTO = new EquipamentoPageDTO(equipamentos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar Novo Equipamento com sucesso")
    void salvarNovoEquipametoComSucesso() {
        when(service.salvar(any())).thenReturn(equipamentoDTO);

        EquipamentoDTO response = service.salvar(mapper.toDTO(equipamento));

        assertNotNull(response);
        assertEquals(EquipamentoDTO.class, response.getClass());
        assertEquals(equipamento.getId(), response.id());
        assertEquals(equipamento.getDescricao(), response.descricao());
        assertEquals(equipamento.getSuperior(), response.superior());
        assertEquals(equipamento.getInferior(), response.inferior());
        assertEquals(equipamento.getDireita(), response.direita());
        assertEquals(equipamento.getEsquerda(), response.esquerda());
        assertEquals(equipamento.getModLaudo(), response.modLaudo());
        assertEquals(equipamento.getCidade(), response.cidade());
        assertEquals(equipamento.getOrdena(), response.ordena());

        verify(mapper, times(1)).toDTO(equipamento);

        assertThat(response).usingRecursiveComparison().isEqualTo(equipamentoDTO);
        assertThat(response.id()).isEqualTo(equipamentoDTO.id());
        assertThat(response.descricao()).isEqualTo(equipamentoDTO.descricao());
        assertThat(response.superior()).isEqualTo(equipamentoDTO.superior());
        assertThat(response.inferior()).isEqualTo(equipamentoDTO.inferior());
        assertThat(response.direita()).isEqualTo(equipamentoDTO.direita());
        assertThat(response.esquerda()).isEqualTo(equipamentoDTO.esquerda());
        assertThat(response.modLaudo()).isEqualTo(equipamentoDTO.modLaudo());
        assertThat(response.cidade()).isEqualTo(equipamentoDTO.cidade());
        assertThat(response.ordena()).isEqualTo(equipamentoDTO.ordena());
    }

    @Test
    @DisplayName(value = "Atualizar Equipamento com sucesso")
    void atualizarEquipamentoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(equipamento1DTO);

        EquipamentoDTO response = service.update(equipamento1DTO.id(), equipamento1DTO);

        assertNotNull(response);
        assertEquals(EquipamentoDTO.class, equipamento1DTO.getClass());
        assertEquipamentoDTO(equipamento1DTO, response);

        assertThat(response).usingRecursiveComparison().isEqualTo(equipamento1DTO);
        assertThat(response.id()).isEqualTo(equipamento1DTO.id());
        assertThat(response.descricao()).isEqualTo(equipamento1DTO.descricao());
        assertThat(response.superior()).isEqualTo(equipamento1DTO.superior());
        assertThat(response.inferior()).isEqualTo(equipamento1DTO.inferior());
        assertThat(response.direita()).isEqualTo(equipamento1DTO.direita());
        assertThat(response.esquerda()).isEqualTo(equipamento1DTO.esquerda());
        assertThat(response.modLaudo()).isEqualTo(equipamento1DTO.modLaudo());
        assertThat(response.cidade()).isEqualTo(equipamento1DTO.cidade());
        assertThat(response.ordena()).isEqualTo(equipamento1DTO.ordena());
    }

    @Test
    @DisplayName(value = "Apagar Equipamento por id informado.")
    void apagarEquipamentoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(equipamento1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todos os equipamentos com sucesso.")
    void listarTodosEquipamentosComSucesso() {
        when(service.findAll(anyInt(), anyInt())).thenReturn(equipamentoPageDTO);

        equipamentos.add(equipamentoDTO);

        EquipamentoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(EquipamentoPageDTO.class, response.getClass());
        assertEquals(EquipamentoDTO.class, response.equipamentos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(equipamentoDTO.id(), response.equipamentos().get(0).id());
        assertEquals(equipamentoDTO.descricao(), response.equipamentos().get(0).descricao());
        assertEquals(equipamentoDTO.superior(), response.equipamentos().get(0).superior());
        assertEquals(equipamentoDTO.inferior(), response.equipamentos().get(0).inferior());
        assertEquals(equipamentoDTO.direita(), response.equipamentos().get(0).direita());
        assertEquals(equipamentoDTO.modLaudo(), response.equipamentos().get(0).modLaudo());
        assertEquals(equipamentoDTO.cidade(), response.equipamentos().get(0).cidade());
        assertEquals(equipamentoDTO.ordena(), response.equipamentos().get(0).ordena());
    }

    @Test
    @DisplayName(value = "Recuperar equipamento por Id com sucesso.")
    void recuperarEquipamentoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(equipamento1DTO);

        EquipamentoDTO response = service.findById(equipamento1.getId());

        assertNotNull(response);
        assertEquals(EquipamentoDTO.class, response.getClass());
        assertEquipamentoDTO(equipamento1DTO, response);
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar equipamento por um ID invalido.")
    void gerarExcecaoQuandoPesquisarEquipamentoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    private void assertEquipamentoDTO(EquipamentoDTO equipamento1DTO, EquipamentoDTO response) {
        assertEquals(equipamento1DTO.id(), response.id());
        assertEquals(equipamento1DTO.descricao(), response.descricao());
        assertEquals(equipamento1DTO.superior(), response.superior());
        assertEquals(equipamento1DTO.inferior(), response.inferior());
        assertEquals(equipamento1DTO.direita(), response.direita());
        assertEquals(equipamento1DTO.esquerda(), response.esquerda());
        assertEquals(equipamento1DTO.modLaudo(), response.modLaudo());
        assertEquals(equipamento1DTO.cidade(), response.cidade());
        assertEquals(equipamento1DTO.ordena(), response.ordena());    }
}
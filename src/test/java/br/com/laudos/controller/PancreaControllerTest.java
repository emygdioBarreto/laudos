package br.com.laudos.controller;

import br.com.laudos.domain.Pancrea;
import br.com.laudos.dto.PancreaDTO;
import br.com.laudos.dto.mapper.PancreaMapper;
import br.com.laudos.dto.pages.PancreaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.PancreaService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PancreaControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de pancreas não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private PancreaController controller;

    @Mock
    private PancreaService service;

    @Mock
    private PancreaMapper mapper;

    private Pancrea pancrea;
    private PancreaDTO pancreaDTO;
    private Pancrea pancrea1;
    private PancreaDTO pancreas1DTO;

    private final List<PancreaDTO> pancreas = new ArrayList<>();
    private PancreaPageDTO pancreasPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pancrea = new Pancrea(null, "Teste de gravação de frase de Pancreas");
        pancreaDTO = new PancreaDTO(null, "Teste de gravação de frase de Pancreas");
        pancrea1 = new Pancrea(5, "teste de frase de pancreas");
        pancreas1DTO = new PancreaDTO(5, "teste de frase de pancreas Teste!");

        pancreas.add(pancreaDTO);
        pancreasPageDTO = new PancreaPageDTO(pancreas, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de pancreas com sucesso")
    void salvarFraseDePancreasComSucesso() {
        when(service.salvar(any())).thenReturn(pancreaDTO);

        PancreaDTO response = service.salvar(mapper.toDTO(pancrea));

        assertNotNull(response);
        assertEquals(PancreaDTO.class, response.getClass());
        assertEquals(pancrea.getId(), response.id());
        assertEquals(pancrea.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(pancrea);

        assertThat(response).usingRecursiveComparison().isEqualTo(pancreaDTO);
        assertThat(response.descricao()).isEqualTo(pancreaDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de pancreas com sucesso")
    void atualizarFrasePancreasComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(pancreas1DTO);

        PancreaDTO response = service.update(pancreas1DTO.id(), pancreas1DTO);

        assertNotNull(response);
        assertEquals(PancreaDTO.class, pancreas1DTO.getClass());
        assertEquals(pancreas1DTO.id(), response.id());
        assertEquals(pancreas1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(pancreas1DTO);
        assertThat(response.descricao()).isEqualTo(pancreas1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de pancreas por id informado.")
    void apagarFrasePancreasPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(pancreas1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de pancreas com sucesso.")
    void listarTodasFrasesPancreasComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(pancreasPageDTO);

        pancreas.add(pancreaDTO);
        PancreaPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(PancreaPageDTO.class, response.getClass());
        assertEquals(PancreaDTO.class, response.pancreas().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(pancreaDTO.id(), response.pancreas().get(0).id());
        assertEquals(pancreaDTO.descricao(), response.pancreas().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de pancreas por Id com sucesso.")
    void recuperarFrasePancreasPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(pancreas1DTO);

        PancreaDTO response = service.findById(pancrea1.getId());

        assertNotNull(response);
        assertEquals(PancreaDTO.class, response.getClass());
        assertEquals(pancreas1DTO.id(), response.id());
        assertEquals(pancreas1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar pancreas por um ID invalido.")
    void gerarExcecaoQuandoPesquisarPancreasPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
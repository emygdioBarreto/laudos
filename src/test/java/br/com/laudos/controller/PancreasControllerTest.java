package br.com.laudos.controller;

import br.com.laudos.domain.Pancreas;
import br.com.laudos.dto.PancreasDTO;
import br.com.laudos.dto.mapper.PancreasMapper;
import br.com.laudos.dto.pages.PancreasPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.PancreasService;
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
class PancreasControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de pancreas não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private PancreasController controller;

    @Mock
    private PancreasService service;

    @Mock
    private PancreasMapper mapper;

    private Pancreas pancreas;
    private PancreasDTO pancreasDTO;
    private Pancreas pancreas1;
    private PancreasDTO pancreas1DTO;

    private final List<PancreasDTO> pancreass = new ArrayList<>();
    private PancreasPageDTO pancreasPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pancreas = new Pancreas(null, "Teste de gravação de frase de Pancreas");
        pancreasDTO = new PancreasDTO(null, "Teste de gravação de frase de Pancreas");
        pancreas1 = new Pancreas(5, "teste de frase de pancreas");
        pancreas1DTO = new PancreasDTO(5, "teste de frase de pancreas Teste!");

        pancreass.add(pancreasDTO);
        pancreasPageDTO = new PancreasPageDTO(pancreass, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de pancreas com sucesso")
    void salvarFraseDePancreasComSucesso() {
        when(service.salvar(any())).thenReturn(pancreasDTO);

        PancreasDTO response = service.salvar(mapper.toDTO(pancreas));

        assertNotNull(response);
        assertEquals(PancreasDTO.class, response.getClass());
        assertEquals(pancreas.getId(), response.id());
        assertEquals(pancreas.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(pancreas);

        assertThat(response).usingRecursiveComparison().isEqualTo(pancreasDTO);
        assertThat(response.descricao()).isEqualTo(pancreasDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de pancreas com sucesso")
    void atualizarFrasePancreasComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(pancreas1DTO);

        PancreasDTO response = service.update(pancreas1DTO.id(), pancreas1DTO);

        assertNotNull(response);
        assertEquals(PancreasDTO.class, pancreas1DTO.getClass());
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

        pancreass.add(pancreasDTO);
        PancreasPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(PancreasPageDTO.class, response.getClass());
        assertEquals(PancreasDTO.class, response.pancreass().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(pancreasDTO.id(), response.pancreass().get(0).id());
        assertEquals(pancreasDTO.descricao(), response.pancreass().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de pancreas por Id com sucesso.")
    void recuperarFrasePancreasPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(pancreas1DTO);

        PancreasDTO response = service.findById(pancreas1.getId());

        assertNotNull(response);
        assertEquals(PancreasDTO.class, response.getClass());
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
package br.com.laudos.controller;

import br.com.laudos.domain.TipoExame;
import br.com.laudos.dto.TipoExameDTO;
import br.com.laudos.dto.mapper.TipoExameMapper;
import br.com.laudos.dto.pages.TipoExamePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.TipoExameService;
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
class TipoExameControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Tipo de exame não encontrado pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private TipoExameController controller;

    @Mock
    private TipoExameService service;

    @Mock
    private TipoExameMapper mapper;

    private TipoExame tipoExame;
    private TipoExameDTO tipoExameDTO;
    private TipoExame tipoExame1;
    private TipoExameDTO tipoExame1DTO;

    private final List<TipoExameDTO> tipoExames = new ArrayList<>();
    private TipoExamePageDTO tipoExamePageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoExame = new TipoExame(null, "Teste de gravação de Tipos de Exames", "C", false, false, false, false, false);
        tipoExameDTO = new TipoExameDTO(null, "Teste de gravação de Tipos de Exames", "C", false, false, false, false, false);
        tipoExame1 = new TipoExame(1, "Endoscopia Digestiva Alta", "C", true, true, true, false, false);
        tipoExame1DTO = new TipoExameDTO(1, "Endoscopia Digestiva Alta Teste!", "C", true, true, true, false, false);

        tipoExames.add(tipoExameDTO);
        tipoExamePageDTO = new TipoExamePageDTO(tipoExames, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar tipo de exame com sucesso")
    void salvarFraseDeTipoExameComSucesso() {
        when(service.salvar(any())).thenReturn(tipoExameDTO);

        TipoExameDTO response = service.salvar(mapper.toDTO(tipoExame));

        assertNotNull(response);
        assertEquals(TipoExameDTO.class, response.getClass());
        assertEquals(tipoExame.getId(), response.id());
        assertEquals(tipoExame.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(tipoExame);

        assertThat(response).usingRecursiveComparison().isEqualTo(tipoExameDTO);
        assertThat(response.descricao()).isEqualTo(tipoExameDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar tipo de exame com sucesso")
    void atualizarFraseTipoExameComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(tipoExame1DTO);

        TipoExameDTO response = service.update(tipoExame1DTO.id(), tipoExame1DTO);

        assertNotNull(response);
        assertEquals(TipoExameDTO.class, tipoExame1DTO.getClass());
        assertEquals(tipoExame1DTO.id(), response.id());
        assertEquals(tipoExame1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(tipoExame1DTO);
        assertThat(response.descricao()).isEqualTo(tipoExame1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar tipo de exame por id informado.")
    void apagarFraseTipoExamePorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(tipoExame1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todos os tipo de exame com sucesso.")
    void listarTodasFrasesTipoExameComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(tipoExamePageDTO);

        tipoExames.add(tipoExameDTO);
        TipoExamePageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(TipoExamePageDTO.class, response.getClass());
        assertEquals(TipoExameDTO.class, response.tipoexames().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(tipoExameDTO.id(), response.tipoexames().get(0).id());
        assertEquals(tipoExameDTO.descricao(), response.tipoexames().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar tipo de exame por Id com sucesso.")
    void recuperarFraseTipoExamePorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(tipoExame1DTO);

        TipoExameDTO response = service.findById(tipoExame1.getId());

        assertNotNull(response);
        assertEquals(TipoExameDTO.class, response.getClass());
        assertEquals(tipoExame1DTO.id(), response.id());
        assertEquals(tipoExame1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar tipo de exame por um ID invalido.")
    void gerarExcecaoQuandoPesquisarTipoExamePorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
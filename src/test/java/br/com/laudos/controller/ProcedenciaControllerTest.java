package br.com.laudos.controller;

import br.com.laudos.domain.Procedencia;
import br.com.laudos.dto.ProcedenciaDTO;
import br.com.laudos.dto.mapper.ProcedenciaMapper;
import br.com.laudos.dto.pages.ProcedenciaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.ProcedenciaService;
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
class ProcedenciaControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de procedência não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private ProcedenciaController controller;

    @Mock
    private ProcedenciaService service;

    @Mock
    private ProcedenciaMapper mapper;

    private Procedencia procedencia;
    private ProcedenciaDTO procedenciaDTO;
    private Procedencia procedencia1;
    private ProcedenciaDTO procedencia1DTO;

    private final List<ProcedenciaDTO> procedencias = new ArrayList<>();
    private ProcedenciaPageDTO procedenciaPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        procedencia = new Procedencia(null, "Teste de gravação de frase de Procedencia");
        procedenciaDTO = new ProcedenciaDTO(null, "Teste de gravação de frase de Procedencia");
        procedencia1 = new Procedencia(3, "Sul América");
        procedencia1DTO = new ProcedenciaDTO(3, "Sul América Teste!");

        procedencias.add(procedenciaDTO);
        procedenciaPageDTO = new ProcedenciaPageDTO(procedencias, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de procedência com sucesso")
    void salvarFraseDeProcedenciaComSucesso() {
        when(service.salvar(any())).thenReturn(procedenciaDTO);

        ProcedenciaDTO response = service.salvar(mapper.toDTO(procedencia));

        assertNotNull(response);
        assertEquals(ProcedenciaDTO.class, response.getClass());
        assertEquals(procedencia.getId(), response.id());
        assertEquals(procedencia.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(procedencia);

        assertThat(response).usingRecursiveComparison().isEqualTo(procedenciaDTO);
        assertThat(response.descricao()).isEqualTo(procedenciaDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de procedência com sucesso")
    void atualizarFraseProcedenciaComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(procedencia1DTO);

        ProcedenciaDTO response = service.update(procedencia1DTO.id(), procedencia1DTO);

        assertNotNull(response);
        assertEquals(ProcedenciaDTO.class, procedencia1DTO.getClass());
        assertEquals(procedencia1DTO.id(), response.id());
        assertEquals(procedencia1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(procedencia1DTO);
        assertThat(response.descricao()).isEqualTo(procedencia1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de procedência por id informado.")
    void apagarFraseProcedenciaPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(procedencia1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de procedência com sucesso.")
    void listarTodasFrasesProcedenciaComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(procedenciaPageDTO);

        procedencias.add(procedenciaDTO);
        ProcedenciaPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(ProcedenciaPageDTO.class, response.getClass());
        assertEquals(ProcedenciaDTO.class, response.procedencias().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(procedenciaDTO.id(), response.procedencias().get(0).id());
        assertEquals(procedenciaDTO.descricao(), response.procedencias().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de procedência por Id com sucesso.")
    void recuperarFraseProcedenciaPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(procedencia1DTO);

        ProcedenciaDTO response = service.findById(procedencia1.getId());

        assertNotNull(response);
        assertEquals(ProcedenciaDTO.class, response.getClass());
        assertEquals(procedencia1DTO.id(), response.id());
        assertEquals(procedencia1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar procedência por um ID invalido.")
    void gerarExcecaoQuandoPesquisarProcedenciaPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.controller;

import br.com.laudos.domain.Solicitante;
import br.com.laudos.dto.SolicitanteDTO;
import br.com.laudos.dto.mapper.SolicitanteMapper;
import br.com.laudos.dto.pages.SolicitantePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.SolicitanteService;
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
class SolicitanteControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de solicitante não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private SolicitanteController controller;

    @Mock
    private SolicitanteService service;

    @Mock
    private SolicitanteMapper mapper;

    private Solicitante solicitante;
    private SolicitanteDTO solicitanteDTO;
    private Solicitante solicitante1;
    private SolicitanteDTO solicitante1DTO;

    private final List<SolicitanteDTO> solicitantes = new ArrayList<>();
    private SolicitantePageDTO solicitantePageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        solicitante = new Solicitante(null, "Teste de gravação de frase de Solicitante");
        solicitanteDTO = new SolicitanteDTO(null, "Teste de gravação de frase de Solicitante");
        solicitante1 = new Solicitante(3, "Dr. Moacyr Novaes");
        solicitante1DTO = new SolicitanteDTO(3, "Dr. Moacyr Novaes Teste!");

        solicitantes.add(solicitanteDTO);
        solicitantePageDTO = new SolicitantePageDTO(solicitantes, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de solicitante com sucesso")
    void salvarFraseDeSolicitanteComSucesso() {
        when(service.salvar(any())).thenReturn(solicitanteDTO);

        SolicitanteDTO response = service.salvar(mapper.toDTO(solicitante));

        assertNotNull(response);
        assertEquals(SolicitanteDTO.class, response.getClass());
        assertEquals(solicitante.getId(), response.id());
        assertEquals(solicitante.getMedicoSolicitante(), response.medicoSolicitante());

        verify(mapper, times(1)).toDTO(solicitante);

        assertThat(response).usingRecursiveComparison().isEqualTo(solicitanteDTO);
        assertThat(response.medicoSolicitante()).isEqualTo(solicitanteDTO.medicoSolicitante());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de solicitante com sucesso")
    void atualizarFraseSolicitanteComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(solicitante1DTO);

        SolicitanteDTO response = service.update(solicitante1DTO.id(), solicitante1DTO);

        assertNotNull(response);
        assertEquals(SolicitanteDTO.class, solicitante1DTO.getClass());
        assertEquals(solicitante1DTO.id(), response.id());
        assertEquals(solicitante1DTO.medicoSolicitante(), response.medicoSolicitante());

        assertThat(response).usingRecursiveComparison().isEqualTo(solicitante1DTO);
        assertThat(response.medicoSolicitante()).isEqualTo(solicitante1DTO.medicoSolicitante());
    }

    @Test
    @DisplayName(value = "Apagar frase de solicitante por id informado.")
    void apagarFraseSolicitantePorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(solicitante1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de solicitante com sucesso.")
    void listarTodasFrasesSolicitanteComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(solicitantePageDTO);

        solicitantes.add(solicitanteDTO);
        SolicitantePageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(SolicitantePageDTO.class, response.getClass());
        assertEquals(SolicitanteDTO.class, response.solicitantes().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(solicitanteDTO.id(), response.solicitantes().get(0).id());
        assertEquals(solicitanteDTO.medicoSolicitante(), response.solicitantes().get(0).medicoSolicitante());
    }

    @Test
    @DisplayName(value = "Recuperar frase de solicitante por Id com sucesso.")
    void recuperarFraseSolicitantePorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(solicitante1DTO);

        SolicitanteDTO response = service.findById(solicitante1.getId());

        assertNotNull(response);
        assertEquals(SolicitanteDTO.class, response.getClass());
        assertEquals(solicitante1DTO.id(), response.id());
        assertEquals(solicitante1DTO.medicoSolicitante(), response.medicoSolicitante());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar solicitante por um ID invalido.")
    void gerarExcecaoQuandoPesquisarSolicitantePorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.controller;

import br.com.laudos.domain.Resumo;
import br.com.laudos.dto.ResumoDTO;
import br.com.laudos.dto.mapper.ResumoMapper;
import br.com.laudos.dto.pages.ResumoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.ResumoService;
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
class ResumoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de resumo clínico não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private ResumoController controller;

    @Mock
    private ResumoService service;

    @Mock
    private ResumoMapper mapper;

    private Resumo resumo;
    private ResumoDTO resumoDTO;
    private Resumo resumo1;
    private ResumoDTO resumo1DTO;

    private final List<ResumoDTO> resumos = new ArrayList<>();
    private ResumoPageDTO resumoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        resumo = new Resumo(null, "Teste de gravação de frase de Resumo");
        resumoDTO = new ResumoDTO(null, "Teste de gravação de frase de Resumo");
        resumo1 = new Resumo(2, "resumo clinico Inexistente 39");
        resumo1DTO = new ResumoDTO(2, "resumo clinico Inexistente 39 Teste!");

        resumos.add(resumoDTO);
        resumoPageDTO = new ResumoPageDTO(resumos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de resumo clínico com sucesso")
    void salvarFraseDeResumoComSucesso() {
        when(service.salvar(any())).thenReturn(resumoDTO);

        ResumoDTO response = service.salvar(mapper.toDTO(resumo));

        assertNotNull(response);
        assertEquals(ResumoDTO.class, response.getClass());
        assertEquals(resumo.getId(), response.id());
        assertEquals(resumo.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(resumo);

        assertThat(response).usingRecursiveComparison().isEqualTo(resumoDTO);
        assertThat(response.descricao()).isEqualTo(resumoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de resumo clínico com sucesso")
    void atualizarFraseResumoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(resumo1DTO);

        ResumoDTO response = service.update(resumo1DTO.id(), resumo1DTO);

        assertNotNull(response);
        assertEquals(ResumoDTO.class, resumo1DTO.getClass());
        assertEquals(resumo1DTO.id(), response.id());
        assertEquals(resumo1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(resumo1DTO);
        assertThat(response.descricao()).isEqualTo(resumo1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de resumo clínico por id informado.")
    void apagarFraseResumoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(resumo1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de resumo clínico com sucesso.")
    void listarTodasFrasesResumoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(resumoPageDTO);

        resumos.add(resumoDTO);
        ResumoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(ResumoPageDTO.class, response.getClass());
        assertEquals(ResumoDTO.class, response.resumos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(resumoDTO.id(), response.resumos().get(0).id());
        assertEquals(resumoDTO.descricao(), response.resumos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de resumo clínico por Id com sucesso.")
    void recuperarFraseResumoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(resumo1DTO);

        ResumoDTO response = service.findById(resumo1.getId());

        assertNotNull(response);
        assertEquals(ResumoDTO.class, response.getClass());
        assertEquals(resumo1DTO.id(), response.id());
        assertEquals(resumo1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar resumo clínico por um ID invalido.")
    void gerarExcecaoQuandoPesquisarResumoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
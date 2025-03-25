package br.com.laudos.controller;

import br.com.laudos.domain.Observacao;
import br.com.laudos.dto.ObservacaoDTO;
import br.com.laudos.dto.mapper.ObservacaoMapper;
import br.com.laudos.dto.pages.ObservacaoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.ObservacaoService;
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
class ObservacaoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de observação não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private ObservacaoController controller;

    @Mock
    private ObservacaoService service;

    @Mock
    private ObservacaoMapper mapper;

    private Observacao observacao;
    private ObservacaoDTO observacaoDTO;
    private Observacao observacao1;
    private ObservacaoDTO observacao1DTO;

    private final List<ObservacaoDTO> observacoes = new ArrayList<>();
    private ObservacaoPageDTO observacaoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        observacao = new Observacao(null, "Teste de gravação de frase de Observacao");
        observacaoDTO = new ObservacaoDTO(null, "Teste de gravação de frase de Observacao");
        observacao1 = new Observacao(8, "Não foram realizadas biópsias.");
        observacao1DTO = new ObservacaoDTO(8, "Não foram realizadas biópsias. Teste!");

        observacoes.add(observacaoDTO);
        observacaoPageDTO = new ObservacaoPageDTO(observacoes, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de observação com sucesso")
    void salvarFraseDeObservacaoComSucesso() {
        when(service.salvar(any())).thenReturn(observacaoDTO);

        ObservacaoDTO response = service.salvar(mapper.toDTO(observacao));

        assertNotNull(response);
        assertEquals(ObservacaoDTO.class, response.getClass());
        assertEquals(observacao.getId(), response.id());
        assertEquals(observacao.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(observacao);

        assertThat(response).usingRecursiveComparison().isEqualTo(observacaoDTO);
        assertThat(response.descricao()).isEqualTo(observacaoDTO.descricao());
   }

    @Test
    @DisplayName(value = "Atualizar a frase de observação com sucesso")
    void atualizarFraseObservacaoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(observacao1DTO);

        ObservacaoDTO response = service.update(observacao1DTO.id(), observacao1DTO);

        assertNotNull(response);
        assertEquals(ObservacaoDTO.class, observacao1DTO.getClass());
        assertEquals(observacao1DTO.id(), response.id());
        assertEquals(observacao1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(observacao1DTO);
        assertThat(response.descricao()).isEqualTo(observacao1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de observação por id informado.")
    void apagarFraseObservacaoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(observacao1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de observação com sucesso.")
    void listarTodasFrasesObservacaoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(observacaoPageDTO);

        observacoes.add(observacaoDTO);
        ObservacaoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(ObservacaoPageDTO.class, response.getClass());
        assertEquals(ObservacaoDTO.class, response.observacoes().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(observacaoDTO.id(), response.observacoes().get(0).id());
        assertEquals(observacaoDTO.descricao(), response.observacoes().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de observação por Id com sucesso.")
    void recuperarFraseObservacaoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(observacao1DTO);

        ObservacaoDTO response = service.findById(observacao1.getId());

        assertNotNull(response);
        assertEquals(ObservacaoDTO.class, response.getClass());
        assertEquals(observacao1DTO.id(), response.id());
        assertEquals(observacao1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar observacao por um ID invalido.")
    void gerarExcecaoQuandoPesquisarObservacaoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
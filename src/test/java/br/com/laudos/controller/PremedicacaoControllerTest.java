package br.com.laudos.controller;

import br.com.laudos.domain.Premedicacao;
import br.com.laudos.dto.PremedicacaoDTO;
import br.com.laudos.dto.mapper.PremedicacaoMapper;
import br.com.laudos.dto.pages.PremedicacaoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.PremedicacaoService;
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
class PremedicacaoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de premedicação não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private PremedicacaoController controller;

    @Mock
    private PremedicacaoService service;

    @Mock
    private PremedicacaoMapper mapper;

    private Premedicacao premedicacao;
    private PremedicacaoDTO premedicacaoDTO;
    private Premedicacao premedicacao1;
    private PremedicacaoDTO premedicacao1DTO;

    private final List<PremedicacaoDTO> premedicacoes = new ArrayList<>();
    private PremedicacaoPageDTO premedicacaoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        premedicacao = new Premedicacao(null, "Teste de gravação de frase de Premedicacao");
        premedicacaoDTO = new PremedicacaoDTO(null, "Teste de gravação de frase de Premedicacao");
        premedicacao1 = new Premedicacao(1, "Xylocaína tópica à 10%.");
        premedicacao1DTO = new PremedicacaoDTO(1, "Xylocaína tópica à 10%. Teste!");

        premedicacoes.add(premedicacaoDTO);
        premedicacaoPageDTO = new PremedicacaoPageDTO(premedicacoes, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de premedicação com sucesso")
    void salvarFraseDePremedicacaoComSucesso() {
        when(service.salvar(any())).thenReturn(premedicacaoDTO);

        PremedicacaoDTO response = service.salvar(mapper.toDTO(premedicacao));

        assertNotNull(response);
        assertEquals(PremedicacaoDTO.class, response.getClass());
        assertEquals(premedicacao.getId(), response.id());
        assertEquals(premedicacao.getPremedicacao(), response.premedicacao());

        verify(mapper, times(1)).toDTO(premedicacao);

        assertThat(response).usingRecursiveComparison().isEqualTo(premedicacaoDTO);
        assertThat(response.premedicacao()).isEqualTo(premedicacaoDTO.premedicacao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de premedicação com sucesso")
    void atualizarFrasePremedicacaoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(premedicacao1DTO);

        PremedicacaoDTO response = service.update(premedicacao1DTO.id(), premedicacao1DTO);

        assertNotNull(response);
        assertEquals(PremedicacaoDTO.class, premedicacao1DTO.getClass());
        assertEquals(premedicacao1DTO.id(), response.id());
        assertEquals(premedicacao1DTO.premedicacao(), response.premedicacao());

        assertThat(response).usingRecursiveComparison().isEqualTo(premedicacao1DTO);
        assertThat(response.premedicacao()).isEqualTo(premedicacao1DTO.premedicacao());
    }

    @Test
    @DisplayName(value = "Apagar frase de premedicação por id informado.")
    void apagarFrasePremedicacaoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(premedicacao1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de premedicação com sucesso.")
    void listarTodasFrasesPremedicacaoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(premedicacaoPageDTO);

        premedicacoes.add(premedicacaoDTO);
        PremedicacaoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(PremedicacaoPageDTO.class, response.getClass());
        assertEquals(PremedicacaoDTO.class, response.premedicacoes().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(premedicacaoDTO.id(), response.premedicacoes().get(0).id());
        assertEquals(premedicacaoDTO.premedicacao(), response.premedicacoes().get(0).premedicacao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de premedicação por Id com sucesso.")
    void recuperarFrasePremedicacaoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(premedicacao1DTO);

        PremedicacaoDTO response = service.findById(premedicacao1.getId());

        assertNotNull(response);
        assertEquals(PremedicacaoDTO.class, response.getClass());
        assertEquals(premedicacao1DTO.id(), response.id());
        assertEquals(premedicacao1DTO.premedicacao(), response.premedicacao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar premedicacao por um ID invalido.")
    void gerarExcecaoQuandoPesquisarPremedicacaoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
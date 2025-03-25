package br.com.laudos.controller;

import br.com.laudos.domain.Esofago;
import br.com.laudos.dto.EsofagoDTO;
import br.com.laudos.dto.mapper.EsofagoMapper;
import br.com.laudos.dto.pages.EsofagoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.EsofagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EsofagoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de esôfago não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private EsofagoController controller;

    @Mock
    private EsofagoService service;

    @Mock
    private EsofagoMapper mapper;

    private Esofago esofago;
    private EsofagoDTO esofagoDTO;
    private Esofago esofago1;
    private EsofagoDTO esofago1DTO;

    private final List<EsofagoDTO> esofagos = new ArrayList<>();
    private EsofagoPageDTO esofagoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        esofago = new Esofago(null, "Teste de gravação de frase de Esôfago");
        esofagoDTO = new EsofagoDTO(null, "Teste de gravação de frase de Esôfago");
        esofago1 = new Esofago(2, "Morfologia conservada");
        esofago1DTO = new EsofagoDTO(2, "Morfologia conservada!");

        esofagos.add(esofagoDTO);
        esofagoPageDTO = new EsofagoPageDTO(esofagos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de esôfago com sucesso")
    void salvarFraseDeEsofagoComSucesso() {
        when(service.salvar(any())).thenReturn(esofagoDTO);

        EsofagoDTO response = service.salvar(mapper.toDTO(esofago));

        assertNotNull(response);
        assertEquals(EsofagoDTO.class, response.getClass());
        assertEquals("Teste de gravação de frase de Esôfago", response.descricao());

        verify(mapper, times(1)).toDTO(esofago);

        assertThat(response).usingRecursiveComparison().isEqualTo(esofagoDTO);
        assertThat(response.descricao()).isEqualTo(esofagoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de esôfago com sucesso")
    void atualizarFraseEsofagoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(esofago1DTO);

        EsofagoDTO response = service.update(esofago1DTO.id(), esofago1DTO);

        assertNotNull(response);
        assertEquals(EsofagoDTO.class, esofago1DTO.getClass());
        assertEquals(esofago1DTO.id(), response.id());
        assertEquals(esofago1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(esofago1DTO);
        assertThat(response.descricao()).isEqualTo(esofago1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de esôfago por id informado.")
    void apagarFraseEsofagoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(esofago1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de esôfago com sucesso.")
    void listarTodasFrasesEsofagoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(esofagoPageDTO);

        esofagos.add(esofagoDTO);
        EsofagoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(EsofagoPageDTO.class, response.getClass());
        assertEquals(EsofagoDTO.class, response.esofagos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(esofagoDTO.id(), response.esofagos().get(0).id());
        assertEquals(esofagoDTO.descricao(), response.esofagos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de esôfago por Id com sucesso.")
    void recuperarFraseEsofagoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(esofago1DTO);

        EsofagoDTO response = service.findById(esofago1.getId());

        assertNotNull(response);
        assertEquals(EsofagoDTO.class, response.getClass());
        assertEquals(esofago1DTO.id(), response.id());
        assertEquals(esofago1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar esôfago por um ID invalido.")
    void gerarExcecaoQuandoPesquisarEsofagoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
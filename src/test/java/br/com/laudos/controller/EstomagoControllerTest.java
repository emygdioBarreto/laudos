package br.com.laudos.controller;

import br.com.laudos.domain.Estomago;
import br.com.laudos.dto.EstomagoDTO;
import br.com.laudos.dto.mapper.EstomagoMapper;
import br.com.laudos.dto.pages.EstomagoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.EstomagoService;
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
class EstomagoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de estômago não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private EstomagoController controller;

    @Mock
    private EstomagoService service;

    @Mock
    private EstomagoMapper mapper;

    private Estomago estomago;
    private EstomagoDTO estomagoDTO;
    private Estomago estomago1;
    private EstomagoDTO estomago1DTO;

    private final List<EstomagoDTO> estomagos = new ArrayList<>();
    private EstomagoPageDTO estomagoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        estomago = new Estomago(null, "Teste de gravação de frase de estomago");
        estomagoDTO = new EstomagoDTO(null, "Teste de gravação de frase de estomago");
        estomago1 = new Estomago(1, "Pequena quantidade de líquido de estase claro");
        estomago1DTO = new EstomagoDTO(1, "Pequena quantidade de líquido de estase claro. Teste");

        estomagos.add(estomagoDTO);
        estomagoPageDTO = new EstomagoPageDTO(estomagos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de estômago com sucesso")
    void salvarFraseEstomagoComSucesso() {
        when(service.salvar(any())).thenReturn(estomagoDTO);

        EstomagoDTO response = service.salvar(mapper.toDTO(estomago));

        assertNotNull(response);
        assertEquals(EstomagoDTO.class, response.getClass());
        assertEquals(estomago.getId(), response.id());
        assertEquals(estomago.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(estomago);

        assertThat(response).usingRecursiveComparison().isEqualTo(estomagoDTO);
        assertThat(response.descricao()).isEqualTo(estomagoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de estômago com sucesso")
    void atualizarFraseEstomagoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(estomago1DTO);

        EstomagoDTO response = service.update(estomago1DTO.id(), estomago1DTO);

        assertNotNull(response);
        assertEquals(EstomagoDTO.class, estomago1DTO.getClass());
        assertEquals(estomago1DTO.id(), response.id());
        assertEquals(estomago1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(estomago1DTO);
        assertThat(response.descricao()).isEqualTo(estomago1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de estômago por id informado.")
    void apagarFraseEstomagoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(estomago1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de estômago com sucesso.")
    void listarTodasFrasesEstomagoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(estomagoPageDTO);

        estomagos.add(estomagoDTO);
        EstomagoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(EstomagoPageDTO.class, response.getClass());
        assertEquals(EstomagoDTO.class, response.estomagos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(estomagoDTO.id(), response.estomagos().get(0).id());
        assertEquals(estomagoDTO.descricao(), response.estomagos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de estômago por Id com sucesso.")
    void recuperarFraseEstomagoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(estomago1DTO);

        EstomagoDTO response = service.findById(estomago1.getId());

        assertNotNull(response);
        assertEquals(EstomagoDTO.class, response.getClass());
        assertEquals(estomago1DTO.id(), response.id());
        assertEquals(estomago1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar estômago por um ID invalido.")
    void gerarExcecaoQuandoPesquisarEstomagoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
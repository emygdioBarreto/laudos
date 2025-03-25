package br.com.laudos.controller;

import br.com.laudos.domain.Intestino;
import br.com.laudos.dto.IntestinoDTO;
import br.com.laudos.dto.mapper.IntestinoMapper;
import br.com.laudos.dto.pages.IntestinoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.IntestinoService;
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
class IntestinoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de intestino não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private IntestinoController controller;

    @Mock
    private IntestinoService service;

    @Mock
    private IntestinoMapper mapper;

    private Intestino intestino;
    private IntestinoDTO intestinoDTO;
    private Intestino intestino1;
    private IntestinoDTO intestino1DTO;

    private final List<IntestinoDTO> intestinos = new ArrayList<>();
    private IntestinoPageDTO intestinoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        intestino = new Intestino(null, "Teste de gravação de frase de Intestino");
        intestinoDTO = new IntestinoDTO(null, "Teste de gravação de frase de Intestino");
        intestino1 = new Intestino(2, "Teste de frase de intestino grosso");
        intestino1DTO = new IntestinoDTO(2, "Teste de frase de intestino grosso teste!");

        intestinos.add(intestinoDTO);
        intestinoPageDTO = new IntestinoPageDTO(intestinos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de intestino com sucesso")
    void salvarFraseIntestinoComSucesso() {
        when(service.salvar(any())).thenReturn(intestinoDTO);

        IntestinoDTO response = service.salvar(mapper.toDTO(intestino));

        assertNotNull(response);
        assertEquals(IntestinoDTO.class, response.getClass());
        assertEquals(intestino.getId(), response.id());
        assertEquals(intestino.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(intestino);

        assertThat(response).usingRecursiveComparison().isEqualTo(intestinoDTO);
        assertThat(response.descricao()).isEqualTo(intestinoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de intestino com sucesso")
    void atualizarFraseIntestinoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(intestino1DTO);

        IntestinoDTO response = service.update(intestino1DTO.id(), intestino1DTO);

        assertNotNull(response);
        assertEquals(IntestinoDTO.class, intestino1DTO.getClass());
        assertEquals(intestino1DTO.id(), response.id());
        assertEquals(intestino1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(intestino1DTO);
        assertThat(response.descricao()).isEqualTo(intestino1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de intestino por id informado.")
    void apagarFraseIntestinoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(intestino1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de intestino com sucesso.")
    void listarTodasFrasesIntestinoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(intestinoPageDTO);

        intestinos.add(intestinoDTO);
        IntestinoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(IntestinoPageDTO.class, response.getClass());
        assertEquals(IntestinoDTO.class, response.intestinos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(intestinoDTO.id(), response.intestinos().get(0).id());
        assertEquals(intestinoDTO.descricao(), response.intestinos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de duodeintestinono por Id com sucesso.")
    void recuperarFraseIntestinoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(intestino1DTO);

        IntestinoDTO response = service.findById(intestino1.getId());

        assertNotNull(response);
        assertEquals(IntestinoDTO.class, response.getClass());
        assertEquals(intestino1DTO.id(), response.id());
        assertEquals(intestino1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar intestino por um ID invalido.")
    void gerarExcecaoQuandoPesquisarIntestinoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
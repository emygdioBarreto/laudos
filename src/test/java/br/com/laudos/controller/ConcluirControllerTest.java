package br.com.laudos.controller;

import br.com.laudos.domain.Concluir;
import br.com.laudos.dto.ConcluirDTO;
import br.com.laudos.dto.mapper.ConcluirMapper;
import br.com.laudos.dto.pages.ConcluirPageDTO;
import br.com.laudos.service.ConcluirService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcluirControllerTest {

    private static final Integer ID = 1;
    private static final String CONCLUSAO = "Exame normal do ponto de vista endoscópico.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    private Concluir newConcluir;
    private ConcluirDTO concluirDTO;
    private ConcluirDTO concluirUpdate;
    private final List<ConcluirDTO> conclusoes = new ArrayList<>();
    private ConcluirPageDTO concluirPageDTO;

    @InjectMocks
    private ConcluirController controller;

    @Mock
    private ConcluirService service;

    @Mock
    private ConcluirMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        newConcluir = new Concluir(null, "Exame normal do ponto de vista endoscópico 2.");
        concluirDTO = new ConcluirDTO(1, "Exame normal do ponto de vista endoscópico.");
        concluirUpdate = new ConcluirDTO(1, "Esofagite erosiva de refluxo, grau B de Los Angeles");
        conclusoes.add(concluirDTO);
        concluirPageDTO = new ConcluirPageDTO(conclusoes, PAGE, SIZE);
    }

    @Test
    @DisplayName("Salvar uma nova frase de conclusão com sucesso")
    void salvarFraseConcluirComSucesso() {
        when(service.salvar(any())).thenReturn(concluirDTO);

        ConcluirDTO response = service.salvar(mapper.toDTO(newConcluir));

        assertNotNull(response);
        assertEquals(ConcluirDTO.class, response.getClass());
        assertEquals(concluirDTO.id(), response.id());
        assertEquals(concluirDTO.conclusao(), response.conclusao());

        verify(service, times(1)).salvar(any());
        verify(mapper, times(1)).toDTO(newConcluir);

        assertThat(response).usingRecursiveComparison().isEqualTo(concluirDTO);
        assertThat(response.conclusao()).isEqualTo(concluirDTO.conclusao());
    }

    @Test
    @DisplayName("Atualizar uma nova frase de conclusão com sucesso")
    void atualizarFraseConcluirComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(concluirUpdate);

        ConcluirDTO response = service.update(1, concluirUpdate);

        assertNotNull(response);
        assertEquals(ConcluirDTO.class, response.getClass());
        assertEquals(1, response.id());
        assertEquals("Esofagite erosiva de refluxo, grau B de Los Angeles", response.conclusao());
    }

    @Test
    @DisplayName("Agagar Frase de concluir com Sucesso")
    void AgagarFraseConcluirComSucesso() {
        doNothing().when(service).delete(anyInt());

        service.delete(1);

        verify(service, times(1)).delete(anyInt());
    }

    @Test
    @DisplayName("Listar todas as frases de concluir com sucesso")
    void ListarTodasFrasesConclusaoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(concluirPageDTO);

        conclusoes.add(concluirDTO);

        ConcluirPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(ConcluirPageDTO.class, response.getClass());
        assertEquals(ConcluirDTO.class, response.conclusoes().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(concluirDTO.id(), response.conclusoes().get(0).id());
        assertEquals(concluirDTO.conclusao(), response.conclusoes().get(0).conclusao());
    }

    @Test
    @DisplayName(value = "Buscar frase de conclusao por ID com sucesso")
    void buscarFraseConclusaoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(concluirDTO);

        var response = service.findById(ID);

        assertNotNull(response);
        assertEquals(ConcluirDTO.class, response.getClass());

        assertEquals(ID, response.id());
        assertEquals(CONCLUSAO, response.conclusao());
    }
}
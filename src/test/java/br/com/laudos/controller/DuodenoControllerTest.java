package br.com.laudos.controller;

import br.com.laudos.domain.Duodeno;
import br.com.laudos.dto.DuodenoDTO;
import br.com.laudos.dto.mapper.DuodenoMapper;
import br.com.laudos.dto.pages.DuodenoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.DuodenoService;
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
class DuodenoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de duodeno não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private DuodenoController controller;

    @Mock
    private DuodenoService service;

    @Mock
    private DuodenoMapper mapper;

    private Duodeno duodeno;
    private DuodenoDTO duodenoDTO;
    private Duodeno duodeno1;
    private DuodenoDTO duodeno1DTO;

    private final List<DuodenoDTO> duodenos = new ArrayList<>();
    private DuodenoPageDTO duodenoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        duodeno = new Duodeno(null, "Teste de gravação de frase de Duodeno");
        duodenoDTO = new DuodenoDTO(null, "Teste de gravação de frase de Duodeno");
        duodeno1 = new Duodeno(3, "Papila anatômica");
        duodeno1DTO = new DuodenoDTO(3, "Papila anatômica Teste!");

        duodenos.add(duodenoDTO);
        duodenoPageDTO = new DuodenoPageDTO(duodenos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de duodeno com sucesso")
    void salvarFraseDeDuodenoComSucesso() {
        when(service.salvar(any())).thenReturn(duodenoDTO);

        DuodenoDTO response = service.salvar(mapper.toDTO(duodeno));

        assertNotNull(response);
        assertEquals(DuodenoDTO.class, response.getClass());
        assertEquals(duodeno.getId(), response.id());
        assertEquals(duodeno.getDescricao(), response.descricao());

        verify(service, times(1)).salvar(any());
        verify(mapper, times(1)).toDTO(duodeno);

        assertThat(response).usingRecursiveComparison().isEqualTo(duodenoDTO);
        assertThat(response.descricao()).isEqualTo(duodenoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de duodeno com sucesso")
    void atualizarFraseDuodenoComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(duodeno1DTO);

        DuodenoDTO response = service.update(duodeno1DTO.id(), duodeno1DTO);

        assertNotNull(response);
        assertEquals(DuodenoDTO.class, duodeno1DTO.getClass());
        assertEquals(duodeno1DTO.id(), response.id());
        assertEquals(duodeno1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(duodeno1DTO);
        assertThat(response.descricao()).isEqualTo(duodeno1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de duodeno por id informado.")
    void apagarFraseDuodenoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(duodeno1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de duodeno com sucesso.")
    void listarTodasFrasesDuodenoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(duodenoPageDTO);

        duodenos.add(duodenoDTO);
        DuodenoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(DuodenoPageDTO.class, response.getClass());
        assertEquals(DuodenoDTO.class, response.duodenos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(duodenoDTO.id(), response.duodenos().get(0).id());
        assertEquals(duodenoDTO.descricao(), response.duodenos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de duodeno por Id com sucesso.")
    void recuperarFraseDuodenoPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(duodeno1DTO);

        var response = service.findById(duodeno1.getId());

        assertNotNull(response);
        assertEquals(DuodenoDTO.class, response.getClass());
        assertEquals(duodeno1DTO.id(), response.id());
        assertEquals(duodeno1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar duodeno por um ID invalido.")
    void gerarExcecaoQuandoPesquisarDuodenoPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.controller;

import br.com.laudos.domain.Local;
import br.com.laudos.dto.LocalDTO;
import br.com.laudos.dto.mapper.LocalMapper;
import br.com.laudos.dto.pages.LocalPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.LocalService;
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
class LocalControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Local não encontrado pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private LocalController controller;

    @Mock
    private LocalService service;

    @Mock
    private LocalMapper mapper;

    private Local local;
    private LocalDTO localDTO;
    private Local local1;
    private LocalDTO local1DTO;

    private final List<LocalDTO> locais = new ArrayList<>();
    private LocalPageDTO localPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        local = new Local(null, "Teste de gravação de Local");
        localDTO = new LocalDTO(null, "Teste de gravação de Local");
        local1 = new Local(2, "Boris Berenstein Imagem e Laboratório");
        local1DTO = new LocalDTO(2, "Boris Berenstein Imagem e Laboratório Teste!");

        locais.add(localDTO);
        localPageDTO = new LocalPageDTO(locais, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de local com sucesso")
    void salvarFraseDeLocalComSucesso() {
        when(service.salvar(any())).thenReturn(localDTO);

        LocalDTO response = service.salvar(mapper.toDTO(local));

        assertNotNull(response);
        assertEquals(LocalDTO.class, response.getClass());
        assertEquals(local.getId(), response.id());
        assertEquals(local.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(local);

        assertThat(response).usingRecursiveComparison().isEqualTo(localDTO);
        assertThat(response.descricao()).isEqualTo(localDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de local com sucesso")
    void atualizarFraseLocalComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(local1DTO);

        LocalDTO response = service.update(local1DTO.id(), local1DTO);

        assertNotNull(response);
        assertEquals(LocalDTO.class, local1DTO.getClass());
        assertEquals(local1DTO.id(), response.id());
        assertEquals(local1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(local1DTO);
        assertThat(response.descricao()).isEqualTo(local1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de local por id informado.")
    void apagarFraseLocalPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(local1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de laudo com sucesso.")
    void listarTodasFrasesLocalComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(localPageDTO);

        locais.add(localDTO);
        LocalPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(LocalPageDTO.class, response.getClass());
        assertEquals(LocalDTO.class, response.locais().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(localDTO.id(), response.locais().get(0).id());
        assertEquals(localDTO.descricao(), response.locais().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de laudo por Id com sucesso.")
    void recuperarFraseLocalPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(local1DTO);

        LocalDTO response = service.findById(local1.getId());

        assertNotNull(response);
        assertEquals(LocalDTO.class, response.getClass());
        assertEquals(local1DTO.id(), response.id());
        assertEquals(local1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar laudo por um ID invalido.")
    void gerarExcecaoQuandoPesquisarLocalPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
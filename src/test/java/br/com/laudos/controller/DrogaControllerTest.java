package br.com.laudos.controller;

import br.com.laudos.domain.Droga;
import br.com.laudos.dto.DrogaDTO;
import br.com.laudos.dto.mapper.DrogaMapper;
import br.com.laudos.dto.pages.DrogaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.DrogaService;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DrogaControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de duodeno não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private DrogaController controller;

    @Mock
    private DrogaService service;

    @Mock
    private DrogaMapper mapper;

    @Mock
    private DrogaPageDTO pageDTO;

    private Droga droga;
    private DrogaDTO drogaDTO;
    private Droga droga1;
    private DrogaDTO droga1DTO;

    private final List<DrogaDTO> drogas = new ArrayList<>();
    private DrogaPageDTO drogaPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        droga = new Droga(null, "Teste de gravação de frase de Duodeno");
        drogaDTO = new DrogaDTO(null, "Teste de gravação de frase de Duodeno");
        droga1 = new Droga(3, "Papila anatômica");
        droga1DTO = new DrogaDTO(3, "Papila anatômica teste!");

        drogas.add(drogaDTO);
        drogaPageDTO = new DrogaPageDTO(drogas, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar uma nova frase de droga com sucesso")
    void salvarFraseDrogaComSucesso() {
        when(service.salvar(any())).thenReturn(drogaDTO);

        DrogaDTO response = service.salvar(mapper.toDTO(droga));

        assertNotNull(response);
        assertEquals(DrogaDTO.class, response.getClass());
        assertEquals(drogaDTO.nomedroga(), response.nomedroga());

        verify(mapper, times(1)).toDTO(droga);

        assertThat(response).usingRecursiveComparison().isEqualTo(drogaDTO);
        assertThat(response.nomedroga()).isEqualTo(drogaDTO.nomedroga());
    }

    @Test
    @DisplayName(value = "Atualizar uma nova frase de droga com sucesso")
    void atualizarFraseDrogaComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(droga1DTO);

        DrogaDTO response = service.update(droga1DTO.id(), droga1DTO);

        assertNotNull(response);
        assertEquals(DrogaDTO.class, droga1DTO.getClass());
        assertEquals(droga1DTO.id(), response.id());
        assertEquals(droga1DTO.nomedroga(), response.nomedroga());

        assertThat(response).usingRecursiveComparison().isEqualTo(droga1DTO);
        assertThat(response.nomedroga()).isEqualTo(droga1DTO.nomedroga());
    }

    @Test
    @DisplayName(value = "Agagar Frase de droga Com Sucesso")
    void apagarFraseDrogaComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(droga1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Buscar todas as frase de drogas com sucesso")
    void ListarTodasFrasesDrogasComSucesso() {
        when(service.findAll(anyInt(), anyInt())).thenReturn(drogaPageDTO);

        drogas.add(drogaDTO);
        DrogaPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(DrogaPageDTO.class, response.getClass());
        assertEquals(DrogaDTO.class, response.drogas().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(drogaDTO.id(), response.drogas().get(0).id());
        assertEquals(drogaDTO.nomedroga(), response.drogas().get(0).nomedroga());
    }

    @Test
    @DisplayName(value = "Buscar frase de droga por ID com sucesso")
    void buscarFraseDrogaPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(droga1DTO);

        DrogaDTO response = service.findById(droga1.getId());

        assertNotNull(response);
        assertEquals(DrogaDTO.class, response.getClass());
        assertEquals(droga1DTO.id(), response.id());
        assertEquals(droga1DTO.nomedroga(), response.nomedroga());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar droga por um ID invalido.")
    void gerarExcecaoQuandoPesquisarDrogaPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
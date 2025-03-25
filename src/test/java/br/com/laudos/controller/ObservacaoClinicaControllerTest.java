package br.com.laudos.controller;

import br.com.laudos.domain.ObservacaoClinica;
import br.com.laudos.dto.ObservacaoClinicaDTO;
import br.com.laudos.dto.mapper.ObservacaoClinicaMapper;
import br.com.laudos.dto.pages.ObservacaoClinicaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.ObservacaoClinicaService;
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
class ObservacaoClinicaControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de observação Clinica não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private ObservacaoClinicaController controller;

    @Mock
    private ObservacaoClinicaService service;

    @Mock
    private ObservacaoClinicaMapper mapper;

    private ObservacaoClinica observacaoClinica;
    private ObservacaoClinicaDTO observacaoClinicaDTO;
    private ObservacaoClinica observacaoClinica1;
    private ObservacaoClinicaDTO observacaoClinica1DTO;

    private final List<ObservacaoClinicaDTO> obsClinicas = new ArrayList<>();
    private ObservacaoClinicaPageDTO obsClinicaPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        observacaoClinica = new ObservacaoClinica(null, "Teste de gravação de frase de ObservacaoClinica");
        observacaoClinicaDTO = new ObservacaoClinicaDTO(null, "Teste de gravação de frase de ObservacaoClinica");
        observacaoClinica1 = new ObservacaoClinica(6, "Gastrite ?");
        observacaoClinica1DTO = new ObservacaoClinicaDTO(6, "Gastrite ? Teste!");

        obsClinicas.add(observacaoClinicaDTO);
        obsClinicaPageDTO = new ObservacaoClinicaPageDTO(obsClinicas, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de observação Clínica com sucesso")
    void salvarFraseDeObservacaoClinicaComSucesso() {
        when(service.salvar(any())).thenReturn(observacaoClinicaDTO);

        ObservacaoClinicaDTO response = service.salvar(mapper.toDTO(observacaoClinica));

        assertNotNull(response);
        assertEquals(ObservacaoClinicaDTO.class, response.getClass());
        assertEquals(observacaoClinica.getId(), response.id());
        assertEquals(observacaoClinica.getDescricao(), response.descricao());

        verify(mapper, times(1)).toDTO(observacaoClinica);

        assertThat(response).usingRecursiveComparison().isEqualTo(observacaoClinicaDTO);
        assertThat(response.descricao()).isEqualTo(observacaoClinicaDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de observação Clínica com sucesso")
    void atualizarFraseObservacaoClinicaComSucesso() {
        when(service.update(anyInt(), any())).thenReturn(observacaoClinica1DTO);

        ObservacaoClinicaDTO response = service.update(observacaoClinica1DTO.id(), observacaoClinica1DTO);

        assertNotNull(response);
        assertEquals(ObservacaoClinicaDTO.class, observacaoClinica1DTO.getClass());
        assertEquals(observacaoClinica1DTO.id(), response.id());
        assertEquals(observacaoClinica1DTO.descricao(), response.descricao());

        assertThat(response).usingRecursiveComparison().isEqualTo(observacaoClinica1DTO);
        assertThat(response.descricao()).isEqualTo(observacaoClinica1DTO.descricao());
    }

    @Test
    @DisplayName(value = "Apagar frase de observação Clínica por id informado.")
    void apagarFraseObservacaoClinicaPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(observacaoClinica1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de observação Clínica com sucesso.")
    void listarTodasFrasesObservacaoClinicaComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(obsClinicaPageDTO);

        obsClinicas.add(observacaoClinicaDTO);
        ObservacaoClinicaPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(ObservacaoClinicaPageDTO.class, response.getClass());
        assertEquals(ObservacaoClinicaDTO.class, response.observacoesClinicas().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(observacaoClinicaDTO.id(), response.observacoesClinicas().get(0).id());
        assertEquals(observacaoClinicaDTO.descricao(), response.observacoesClinicas().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de observação Clínica por Id com sucesso.")
    void recuperarFraseObservacaoClinicaPorIdComSucesso() {
        when(service.findById(anyInt())).thenReturn(observacaoClinica1DTO);

        ObservacaoClinicaDTO response = service.findById(observacaoClinica1DTO.id());

        assertNotNull(response);
        assertEquals(ObservacaoClinicaDTO.class, response.getClass());
        assertEquals(observacaoClinica1DTO.id(), response.id());
        assertEquals(observacaoClinica1DTO.descricao(), response.descricao());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar observação Clínica por um ID invalido.")
    void gerarExcecaoQuandoPesquisarObservacaoClinicaPorIdInvalido() {
        when(service.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.controller;

import br.com.laudos.domain.Medico;
import br.com.laudos.dto.MedicoDTO;
import br.com.laudos.dto.mapper.MedicoMapper;
import br.com.laudos.dto.pages.MedicoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.MedicoService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicoControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Medico não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private MedicoController controller;

    @Mock
    private MedicoService service;

    @Mock
    private MedicoMapper mapper;

    private Medico medico;
    private MedicoDTO medicoDTO;
    private Medico medico1;
    private MedicoDTO medico1DTO;

    private final List<MedicoDTO> medicos = new ArrayList<>();
    private MedicoPageDTO medicoPageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        medico = new Medico(null, "Teste de gravação de Médico executor");
        medicoDTO = new MedicoDTO(null, "Teste de gravação de Médico executor");
        medico1 = new Medico("9062", "Dra. Ana Botler Wilheim");
        medico1DTO = new MedicoDTO("9062", "Dra. Ana Botler Wilheim Teste!");

        medicos.add(medicoDTO);
        medicoPageDTO = new MedicoPageDTO(medicos, PAGE, SIZE);
    }

    @Test
    @DisplayName(value = "Salvar frase de médico com sucesso")
    void salvarFraseDeMedicoComSucesso() {
        when(service.salvar(any())).thenReturn(medicoDTO);

        MedicoDTO response = service.salvar(mapper.toDTO(medico));

        assertNotNull(response);
        assertEquals(MedicoDTO.class, response.getClass());
        assertEquals(medico.getCrm(), response.crm());
        assertEquals(medico.getMedicoExecutor(), response.medicoExecutor());

        verify(mapper, times(1)).toDTO(medico);

        assertThat(response).usingRecursiveComparison().isEqualTo(medicoDTO);
        assertThat(response.medicoExecutor()).isEqualTo(medicoDTO.medicoExecutor());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de medico com sucesso")
    void atualizarFraseMedicoComSucesso() {
        when(service.update(anyString(), any())).thenReturn(medico1DTO);

        MedicoDTO response = service.update(medico1DTO.crm(), medico1DTO);

        assertNotNull(response);
        assertEquals(MedicoDTO.class, medico1DTO.getClass());
        assertEquals(medico1DTO.crm(), response.crm());
        assertEquals(medico1DTO.medicoExecutor(), response.medicoExecutor());

        assertThat(response).usingRecursiveComparison().isEqualTo(medico1DTO);
        assertThat(response.medicoExecutor()).isEqualTo(medico1DTO.medicoExecutor());
    }

    @Test
    @DisplayName(value = "Apagar frase de medico por id informado.")
    void apagarFraseMedicoPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(medico1DTO.crm());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Listar todas as frases de medico com sucesso.")
    void listarTodasFrasesMedicoComSucesso() {
        when(service.findAll(anyInt(),anyInt())).thenReturn(medicoPageDTO);

        medicos.add(medicoDTO);
        MedicoPageDTO response = service.findAll(PAGE, SIZE);

        assertNotNull(response);
        assertEquals(MedicoPageDTO.class, response.getClass());
        assertEquals(MedicoDTO.class, response.medicos().get(0).getClass());
        assertEquals(PAGE, response.totalPages());
        assertEquals(SIZE, response.totalElements());

        assertEquals(medicoDTO.crm(), response.medicos().get(0).crm());
        assertEquals(medicoDTO.medicoExecutor(), response.medicos().get(0).medicoExecutor());
    }

    @Test
    @DisplayName(value = "Recuperar frase de medico por Id com sucesso.")
    void recuperarFraseMedicoPorIdComSucesso() {
        when(service.findById(anyString())).thenReturn(medico1DTO);

        MedicoDTO response = service.findById(medico1.getCrm());

        assertNotNull(response);
        assertEquals(MedicoDTO.class, response.getClass());
        assertEquals(medico1DTO.crm(), response.crm());
        assertEquals(medico1DTO.medicoExecutor(), response.medicoExecutor());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar medico por um ID invalido.")
    void gerarExcecaoQuandoPesquisarMedicoPorIdInvalido() {
        when(service.findById(anyString())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById("1111");
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
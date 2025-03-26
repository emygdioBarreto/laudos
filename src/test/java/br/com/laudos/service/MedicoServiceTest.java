package br.com.laudos.service;

import br.com.laudos.domain.Medico;
import br.com.laudos.dto.MedicoDTO;
import br.com.laudos.dto.mapper.MedicoMapper;
import br.com.laudos.dto.pages.MedicoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.MedicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de medico não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private MedicoService service;

    @Mock
    private MedicoRepository repository;

    @Mock
    private MedicoMapper mapper;

    @Mock
    private MedicoPageDTO pageDTO;

    private Medico medico;
    private MedicoDTO medicoDTO;
    private Medico medico1;
    private MedicoDTO medico1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        medico = new Medico(null, "Teste de gravação de Médico executor");
        medicoDTO = new MedicoDTO(null, "Teste de gravação de frase de Médico executor");
        medico1 = new Medico("9062", "Dra. Ana Botler Wilheim");
        medico1DTO = new MedicoDTO("9062", "Dra. Ana Botler Wilheim Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de medico com sucesso")
    void salvarFraseDeMedicoComSucesso() {
        when(mapper.toEntity(medicoDTO)).thenReturn(medico);
        when(repository.save(any())).thenReturn(medico);
        when(mapper.toDTO(medico)).thenReturn(medicoDTO);

        var varMedico = mapper.toEntity(medicoDTO);
        var varMedicoSaved = repository.save(varMedico);
        var dtoConverter = mapper.toDTO(varMedicoSaved);

        assertNotNull(varMedico);
        assertEquals(MedicoDTO.class, dtoConverter.getClass());
        assertEquals(medicoDTO.medicoExecutor(), dtoConverter.medicoExecutor());

        verify(mapper, times(1)).toEntity(medicoDTO);
        verify(repository, times(1)).save(medico);
        verify(mapper, times(1)).toDTO(medico);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(medicoDTO);
        assertThat(dtoConverter.medicoExecutor()).isEqualTo(medicoDTO.medicoExecutor());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de medico com sucesso")
    void atualizarFraseMedicoComSucesso() {
        when(repository.findById(medico1.getCrm())).thenReturn(Optional.ofNullable(medico1));
        when(repository.save(medico1)).thenReturn(medico1);
        when(mapper.toDTO(medico1)).thenReturn(medico1DTO);

        var varMedico = repository.findById(medico1.getCrm());
        if (varMedico.isPresent()) {
            varMedico.map(med -> {
                med.setMedicoExecutor(medico1DTO.medicoExecutor());
                return mapper.toDTO(repository.save(med));
            });

            assertNotNull(varMedico);
            assertEquals(MedicoDTO.class, medico1DTO.getClass());
            assertEquals(medico1DTO.crm(), varMedico.get().getCrm());
            assertEquals(medico1DTO.medicoExecutor(), varMedico.get().getMedicoExecutor());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de medico por id informado.")
    void apagarFraseMedicoPorIdComSucesso() {
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(medico1));
        doNothing().when(repository).delete(any());

        var varMedico = repository.findById(medico1.getCrm());
        if (varMedico.isPresent()) {
            repository.delete(varMedico.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de medico com sucesso.")
    void listarTodasFrasesMedicoComSucesso() {
        Pageable pageMedico = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "crm"));

        when(repository.findAll()).thenReturn(Collections.singletonList(medico1));
        when(mapper.toDTO(medico1)).thenReturn(medico1DTO);

        List<MedicoDTO> medicos = new ArrayList<>();
        Medico medicoList = repository.findAll().get(0);
        medicos.add(mapper.toDTO(medicoList));

        pageDTO = new MedicoPageDTO(medicos,
                pageMedico.getPageNumber(),
                pageMedico.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), MedicoPageDTO.class);
        assertEquals(pageDTO.medicos().get(0).getClass(), MedicoDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(medico1DTO.crm(), pageDTO.medicos().get(0).crm());
        assertEquals(medico1DTO.medicoExecutor(), pageDTO.medicos().get(0).medicoExecutor());
    }

    @Test
    @DisplayName(value = "Recuperar frase de medico por Id com sucesso.")
    void recuperarFraseMedicoPorIdComSucesso() {
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(medico1));
        when(mapper.toDTO(medico1)).thenReturn(medico1DTO);

        Optional<MedicoDTO> response = repository.findById(medico1.getCrm()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(medico1DTO.crm(), response.get().crm());
            assertEquals(medico1DTO.medicoExecutor(), response.get().medicoExecutor());
        } else {
            throw new RecordNotFoundException(medico1.getCrm());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar medico por um ID invalido.")
    void gerarExcecaoQuandoPesquisarMedicoPorIdInvalido() {
        when(repository.findById(anyString())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById("1111");
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.service;

import br.com.laudos.domain.Procedencia;
import br.com.laudos.dto.ProcedenciaDTO;
import br.com.laudos.dto.mapper.ProcedenciaMapper;
import br.com.laudos.dto.pages.ProcedenciaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ProcedenciaRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcedenciaServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de procedência não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private ProcedenciaService service;

    @Mock
    private ProcedenciaRepository repository;

    @Mock
    private ProcedenciaMapper mapper;

    @Mock
    private ProcedenciaPageDTO pageDTO;

    private Procedencia procedencia;
    private ProcedenciaDTO procedenciaDTO;
    private Procedencia procedencia1;
    private ProcedenciaDTO procedencia1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        procedencia = new Procedencia(null, "Teste de gravação de frase de Procedencia");
        procedenciaDTO = new ProcedenciaDTO(null, "Teste de gravação de frase de Procedencia");
        procedencia1 = new Procedencia(3, "Sul América");
        procedencia1DTO = new ProcedenciaDTO(3, "Sul América Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de procedência com sucesso")
    void salvarFraseDeProcedenciaComSucesso() {
        when(mapper.toEntity(procedenciaDTO)).thenReturn(procedencia);
        when(repository.save(any())).thenReturn(procedencia);
        when(mapper.toDTO(procedencia)).thenReturn(procedenciaDTO);

        var varProcedencia = mapper.toEntity(procedenciaDTO);
        var varProcedenciaSaved = repository.save(varProcedencia);
        var dtoConverter = mapper.toDTO(varProcedenciaSaved);

        assertNotNull(varProcedencia);
        assertEquals(ProcedenciaDTO.class, dtoConverter.getClass());
        assertEquals(procedencia.getDescricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(procedenciaDTO);
        verify(repository, times(1)).save(procedencia);
        verify(mapper, times(1)).toDTO(procedencia);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(procedenciaDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(procedenciaDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de procedência com sucesso")
    void atualizarFraseProcedenciaComSucesso() {
        when(repository.findById(procedencia1.getId())).thenReturn(Optional.ofNullable(procedencia1));
        when(repository.save(procedencia1)).thenReturn(procedencia1);
        when(mapper.toDTO(procedencia1)).thenReturn(procedencia1DTO);

        var varProcedencia = repository.findById(procedencia1.getId());
        if (varProcedencia.isPresent()) {
            varProcedencia.map(duo -> {
                duo.setDescricao(procedencia1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varProcedencia);
            assertEquals(ProcedenciaDTO.class, procedencia1DTO.getClass());
            assertEquals(procedencia1DTO.id(), varProcedencia.get().getId());
            assertEquals(procedencia1DTO.descricao(), varProcedencia.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de procedência por id informado.")
    void apagarFraseProcedenciaPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(procedencia1));
        doNothing().when(repository).delete(any());

        var varProcedencia = repository.findById(procedencia1.getId());
        if (varProcedencia.isPresent()) {
            repository.delete(varProcedencia.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de procedência com sucesso.")
    void listarTodasFrasesProcedenciaComSucesso() {
        Pageable pageProcedencia = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(procedencia1));
        when(mapper.toDTO(procedencia1)).thenReturn(procedencia1DTO);

        List<ProcedenciaDTO> procedencias = new ArrayList<>();
        Procedencia procedenciaList = repository.findAll().get(0);
        procedencias.add(mapper.toDTO(procedenciaList));

        pageDTO = new ProcedenciaPageDTO(procedencias,
                pageProcedencia.getPageNumber(),
                pageProcedencia.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), ProcedenciaPageDTO.class);
        assertEquals(pageDTO.procedencias().get(0).getClass(), ProcedenciaDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(procedencia1DTO.id(), pageDTO.procedencias().get(0).id());
        assertEquals(procedencia1DTO.descricao(), pageDTO.procedencias().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de procedência por Id com sucesso.")
    void recuperarFraseProcedenciaPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(procedencia1));
        when(mapper.toDTO(procedencia1)).thenReturn(procedencia1DTO);

        Optional<ProcedenciaDTO> response = repository.findById(procedencia1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(procedencia1DTO.id(), response.get().id());
            assertEquals(procedencia1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(procedencia1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar procedência por um ID invalido.")
    void gerarExcecaoQuandoPesquisarProcedenciaPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.service;

import br.com.laudos.domain.Solicitante;
import br.com.laudos.dto.SolicitanteDTO;
import br.com.laudos.dto.mapper.SolicitanteMapper;
import br.com.laudos.dto.pages.SolicitantePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.SolicitanteRepository;
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
class SolicitanteServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de solicitante não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private SolicitanteService service;

    @Mock
    private SolicitanteRepository repository;

    @Mock
    private SolicitanteMapper mapper;

    @Mock
    private SolicitantePageDTO pageDTO;

    private Solicitante solicitante;
    private SolicitanteDTO solicitanteDTO;
    private Solicitante solicitante1;
    private SolicitanteDTO solicitante1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        solicitante = new Solicitante(null, "Teste de gravação de frase de Solicitante");
        solicitanteDTO = new SolicitanteDTO(null, "Teste de gravação de frase de Solicitante");
        solicitante1 = new Solicitante(3, "Dr. Moacyr Novaes");
        solicitante1DTO = new SolicitanteDTO(3, "Dr. Moacyr Novaes Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de solicitante com sucesso")
    void salvarFraseDeSolicitanteComSucesso() {
        when(mapper.toEntity(solicitanteDTO)).thenReturn(solicitante);
        when(repository.save(any())).thenReturn(solicitante);
        when(mapper.toDTO(solicitante)).thenReturn(solicitanteDTO);

        var varSolicitante = mapper.toEntity(solicitanteDTO);
        var varSolicitanteSaved = repository.save(varSolicitante);
        var dtoConverter = mapper.toDTO(varSolicitanteSaved);

        assertNotNull(varSolicitante);
        assertEquals(SolicitanteDTO.class, dtoConverter.getClass());
        assertEquals(solicitanteDTO.medicoSolicitante(), dtoConverter.medicoSolicitante());

        verify(mapper, times(1)).toEntity(solicitanteDTO);
        verify(repository, times(1)).save(solicitante);
        verify(mapper, times(1)).toDTO(solicitante);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(solicitanteDTO);
        assertThat(dtoConverter.medicoSolicitante()).isEqualTo(solicitanteDTO.medicoSolicitante());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de solicitante com sucesso")
    void atualizarFraseSolicitanteComSucesso() {
        when(repository.findById(solicitante1.getId())).thenReturn(Optional.ofNullable(solicitante1));
        when(repository.save(solicitante1)).thenReturn(solicitante1);
        when(mapper.toDTO(solicitante1)).thenReturn(solicitante1DTO);

        var varSolicitante = repository.findById(solicitante1.getId());
        if (varSolicitante.isPresent()) {
            varSolicitante.map(duo -> {
                duo.setMedicoSolicitante(solicitante1DTO.medicoSolicitante());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varSolicitante);
            assertEquals(SolicitanteDTO.class, solicitante1DTO.getClass());
            assertEquals(solicitante1DTO.id(), varSolicitante.get().getId());
            assertEquals(solicitante1DTO.medicoSolicitante(), varSolicitante.get().getMedicoSolicitante());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de solicitante por id informado.")
    void apagarFraseSolicitantePorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(solicitante1));
        doNothing().when(repository).delete(any());

        var varSolicitante = repository.findById(solicitante1.getId());
        if (varSolicitante.isPresent()) {
            repository.delete(varSolicitante.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de solicitante com sucesso.")
    void listarTodasFrasesSolicitanteComSucesso() {
        Pageable pageSolicitante = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(solicitante1));
        when(mapper.toDTO(solicitante1)).thenReturn(solicitante1DTO);

        List<SolicitanteDTO> solicitantes = new ArrayList<>();
        Solicitante solicitanteList = repository.findAll().get(0);
        solicitantes.add(mapper.toDTO(solicitanteList));

        pageDTO = new SolicitantePageDTO(solicitantes,
                pageSolicitante.getPageNumber(),
                pageSolicitante.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), SolicitantePageDTO.class);
        assertEquals(pageDTO.solicitantes().get(0).getClass(), SolicitanteDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(solicitante1DTO.id(), pageDTO.solicitantes().get(0).id());
        assertEquals(solicitante1DTO.medicoSolicitante(), pageDTO.solicitantes().get(0).medicoSolicitante());
    }

    @Test
    @DisplayName(value = "Recuperar frase de solicitante por Id com sucesso.")
    void recuperarFraseSolicitantePorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(solicitante1));
        when(mapper.toDTO(solicitante1)).thenReturn(solicitante1DTO);

        Optional<SolicitanteDTO> response = repository.findById(solicitante1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(solicitante1DTO.id(), response.get().id());
            assertEquals(solicitante1DTO.medicoSolicitante(), response.get().medicoSolicitante());
        } else {
            throw new RecordNotFoundException(solicitante1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar solicitante por um ID invalido.")
    void gerarExcecaoQuandoPesquisarSolicitantePorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
package br.com.laudos.service;

import br.com.laudos.domain.Duodeno;
import br.com.laudos.dto.DuodenoDTO;
import br.com.laudos.dto.mapper.DuodenoMapper;
import br.com.laudos.dto.pages.DuodenoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.DuodenoRepository;
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

@ExtendWith(MockitoExtension.class)
class DuodenoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de duodeno não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private DuodenoService service;

    @Mock
    private DuodenoRepository repository;

    @Mock
    private DuodenoMapper mapper;

    @Mock
    private DuodenoPageDTO pageDTO;

    private Duodeno duodeno;
    private DuodenoDTO duodenoDTO;
    private Duodeno duodeno1;
    private DuodenoDTO duodeno1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        duodeno = new Duodeno(null, "Teste de gravação de frase de Duodeno");
        duodenoDTO = new DuodenoDTO(null, "Teste de gravação de frase de Duodeno");
        duodeno1 = new Duodeno(3, "Papila anatômica");
        duodeno1DTO = new DuodenoDTO(3, "Papila anatômica teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de duodeno com sucesso")
    void salvarFraseDeDuodenoComSucesso() {
        when(mapper.toEntity(duodenoDTO)).thenReturn(duodeno);
        when(repository.save(any())).thenReturn(duodeno);
        when(mapper.toDTO(duodeno)).thenReturn(duodenoDTO);

        var varDuodeno = mapper.toEntity(duodenoDTO);
        var varDuodenoSaved = repository.save(varDuodeno);
        var dtoConverter = mapper.toDTO(varDuodenoSaved);

        assertNotNull(varDuodeno);
        assertEquals(DuodenoDTO.class, dtoConverter.getClass());
        assertEquals(duodenoDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(duodenoDTO);
        verify(repository, times(1)).save(duodeno);
        verify(mapper, times(1)).toDTO(duodeno);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(duodenoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(duodenoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de duodeno com sucesso")
    void atualizarFraseDuodenoComSucesso() {
        when(repository.findById(duodeno1.getId())).thenReturn(Optional.ofNullable(duodeno1));
        when(repository.save(duodeno1)).thenReturn(duodeno1);
        when(mapper.toDTO(duodeno1)).thenReturn(duodeno1DTO);

        var varDuodeno = repository.findById(duodeno1.getId());
        if (varDuodeno.isPresent()) {
            varDuodeno.map(duo -> {
                duo.setDescricao(duodeno1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varDuodeno);
            assertEquals(DuodenoDTO.class, duodeno1DTO.getClass());
            assertEquals(duodeno1DTO.id(), varDuodeno.get().getId());
            assertEquals(duodeno1DTO.descricao(), varDuodeno.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de duodeno por id informado.")
    void apagarFraseDuodenoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(duodeno1));
        doNothing().when(repository).delete(any());

        var varDuodeno = repository.findById(duodeno1.getId());
        if (varDuodeno.isPresent()) {
            repository.delete(varDuodeno.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de duodeno com sucesso.")
    void listarTodasFrasesDuodenoComSucesso() {
        Pageable pageDuodeno = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(duodeno1));
        when(mapper.toDTO(duodeno1)).thenReturn(duodeno1DTO);

        List<DuodenoDTO> duodenos = new ArrayList<>();
        Duodeno duodenoList = repository.findAll().get(0);
        duodenos.add(mapper.toDTO(duodenoList));

        pageDTO = new DuodenoPageDTO(duodenos,
                pageDuodeno.getPageNumber(),
                pageDuodeno.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), DuodenoPageDTO.class);
        assertEquals(pageDTO.duodenos().get(0).getClass(), DuodenoDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(duodeno1DTO.id(), pageDTO.duodenos().get(0).id());
        assertEquals(duodeno1DTO.descricao(), pageDTO.duodenos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de duodeno por Id com sucesso.")
    void recuperarFraseDuodenoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(duodeno1));
        when(mapper.toDTO(duodeno1)).thenReturn(duodeno1DTO);

        Optional<DuodenoDTO> response = repository.findById(duodeno1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(duodeno1DTO.id(), response.get().id());
            assertEquals(duodeno1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(duodeno1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar duodeno por um ID invalido.")
    void gerarExcecaoQuandoPesquisarDuodenoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}
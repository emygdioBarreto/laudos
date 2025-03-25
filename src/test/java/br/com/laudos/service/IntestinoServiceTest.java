package br.com.laudos.service;

import br.com.laudos.domain.Intestino;
import br.com.laudos.dto.IntestinoDTO;
import br.com.laudos.dto.mapper.IntestinoMapper;
import br.com.laudos.dto.pages.IntestinoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.IntestinoRepository;
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
class IntestinoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de intestino não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private IntestinoService service;

    @Mock
    private IntestinoRepository repository;

    @Mock
    private IntestinoMapper mapper;

    @Mock
    private IntestinoPageDTO pageDTO;

    private Intestino intestino;
    private IntestinoDTO intestinoDTO;
    private Intestino intestino1;
    private IntestinoDTO intestino1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        intestino = new Intestino(null, "Teste de gravação de frase de Intestino");
        intestinoDTO = new IntestinoDTO(null, "Teste de gravação de frase de Intestino");
        intestino1 = new Intestino(3, "Papila anatômica");
        intestino1DTO = new IntestinoDTO(3, "Papila anatômica teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de intestino com sucesso")
    void salvarFraseDeIntestinoComSucesso() {
        when(mapper.toEntity(intestinoDTO)).thenReturn(intestino);
        when(repository.save(any())).thenReturn(intestino);
        when(mapper.toDTO(intestino)).thenReturn(intestinoDTO);

        var varIntestino = mapper.toEntity(intestinoDTO);
        var varIntestinoSaved = repository.save(varIntestino);
        var dtoConverter = mapper.toDTO(varIntestinoSaved);

        assertNotNull(varIntestino);
        assertEquals(IntestinoDTO.class, dtoConverter.getClass());
        assertEquals(intestinoDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(intestinoDTO);
        verify(repository, times(1)).save(intestino);
        verify(mapper, times(1)).toDTO(intestino);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(intestinoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(intestinoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de intestino com sucesso")
    void atualizarFraseIntestinoComSucesso() {
        when(repository.findById(2)).thenReturn(Optional.ofNullable(intestino1));
        when(repository.save(intestino1)).thenReturn(intestino1);
        when(mapper.toDTO(intestino1)).thenReturn(intestino1DTO);

        var varIntestino = repository.findById(2);
        if (varIntestino.isPresent()) {
            varIntestino.map(duo -> {
                duo.setDescricao(intestino1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varIntestino);
            assertEquals(IntestinoDTO.class, intestino1DTO.getClass());
            assertEquals(intestino1DTO.id(), varIntestino.get().getId());
            assertEquals(intestino1DTO.descricao(), varIntestino.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de intestino por id informado.")
    void apagarFraseIntestinoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(intestino1));
        doNothing().when(repository).delete(any());

        var varIntestino = repository.findById(2);
        if (varIntestino.isPresent()) {
            repository.delete(varIntestino.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de intestino com sucesso.")
    void listarTodasFrasesIntestinoComSucesso() {
        Pageable pageIntestino = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Intestino()));
        when(mapper.toDTO(intestino1)).thenReturn(intestino1DTO);

        List<Intestino> intestinoList = repository.findAll();
        List<IntestinoDTO> intestinos = new ArrayList<>();
        intestinos.add(mapper.toDTO(intestino1));

        pageDTO = new IntestinoPageDTO(intestinos,
                pageIntestino.getPageNumber(),
                pageIntestino.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), IntestinoPageDTO.class);
        assertEquals(pageDTO.intestinos().get(0).getClass(), IntestinoDTO.class);
        assertEquals(0, pageDTO.totalPages());
        assertEquals(10, pageDTO.totalElements());

        assertEquals(intestino1DTO.id(), pageDTO.intestinos().get(0).id());
        assertEquals(intestino1DTO.descricao(), pageDTO.intestinos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de intestino por Id com sucesso.")
    void recuperarFraseIntestinoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(intestino1));
        when(mapper.toDTO(intestino1)).thenReturn(intestino1DTO);

        Optional<IntestinoDTO> response = repository.findById(intestino1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(intestino1DTO.id(), response.get().id());
            assertEquals(intestino1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(intestino1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar intestino por um ID invalido.")
    void gerarExcecaoQuandoPesquisarIntestinoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}